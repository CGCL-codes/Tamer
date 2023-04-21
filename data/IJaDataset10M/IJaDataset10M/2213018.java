package org.apache.myfaces.trinidadinternal.taglib;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.el.ELContext;
import javax.el.PropertyNotWritableException;
import javax.el.ValueExpression;
import javax.el.VariableMapper;
import javax.faces.context.FacesContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.jstl.core.IndexedValueExpression;
import org.apache.myfaces.trinidad.logging.TrinidadLogger;

/**
 *
 */
public class ForEachTag extends TagSupport {

    public void setItems(ValueExpression items) {
        if (items.isLiteralText()) throw new IllegalArgumentException(_LOG.getMessage("MUST_BE_SIMPLE_JSF_EL_EXPRESSION"));
        _items = items;
    }

    public void setBegin(ValueExpression begin) {
        _beginVE = begin;
    }

    public void setEnd(ValueExpression end) {
        _endVE = end;
    }

    public void setStep(ValueExpression step) {
        _stepVE = step;
    }

    public void setVar(String var) {
        _var = var;
    }

    public void setVarStatus(String varStatus) {
        _varStatus = varStatus;
    }

    @Override
    public int doStartTag() throws JspException {
        _validateAttributes();
        FacesContext context = FacesContext.getCurrentInstance();
        _currentBegin = (_begin == null) ? 0 : _begin.intValue();
        _isFirst = true;
        int length;
        if (null != _items) {
            Object items = _items.getValue(context.getELContext());
            if (items == null) {
                if (_LOG.isFine()) _LOG.fine("Items expression " + _items + " resolved to null.");
                return SKIP_BODY;
            }
            _itemsValue = items;
            if (items instanceof List) length = ((List) items).size(); else if (items.getClass().isArray()) length = Array.getLength(items); else throw new JspException(_LOG.getMessage("MUST_POINT_TO_LIST_OR_ARRAY"));
            if (length == 0) {
                if (_LOG.isFine()) _LOG.fine("Items found at " + _items + " is empty.");
                return SKIP_BODY;
            }
            if (length < _currentBegin) {
                if (_LOG.isFine()) _LOG.fine("Size of 'items' is less than 'begin'");
                return SKIP_BODY;
            }
            _currentEnd = (_end == null) ? length - 1 : _end.intValue();
            if (length < _currentEnd) _currentEnd = length - 1;
        } else {
            _currentEnd = (_end == null) ? 0 : _end.intValue();
        }
        _currentIndex = _currentBegin;
        _currentCount = 1;
        _currentStep = (_step == null) ? 1 : _step.intValue();
        _validateRangeAndStep();
        if (_currentEnd < _currentIndex) return SKIP_BODY;
        _isLast = _currentIndex == _currentEnd;
        VariableMapper vm = pageContext.getELContext().getVariableMapper();
        if (_var != null) _previousDeferredVar = vm.resolveVariable(_var);
        if (null != _varStatus) {
            _previousDeferredVarStatus = vm.resolveVariable(_varStatus);
            _propertyReplacementMap = new HashMap<String, Object>(9, 1);
            _propertyReplacementMap.put("begin", Integer.valueOf(_currentBegin));
            _propertyReplacementMap.put("end", Integer.valueOf(_currentEnd));
            _propertyReplacementMap.put("step", Integer.valueOf(_currentStep));
            _propertyReplacementMap.put("count", Integer.valueOf(_currentCount));
            _propertyReplacementMap.put("index", Integer.valueOf(_currentIndex));
            _propertyReplacementMap.put("first", (_isFirst) ? Boolean.TRUE : Boolean.FALSE);
            _propertyReplacementMap.put("last", (_isLast) ? Boolean.TRUE : Boolean.FALSE);
        }
        if (_LOG.isFiner()) {
            _LOG.finer("Iterating from " + _currentIndex + " to " + _currentEnd + " by " + _currentStep);
        }
        _updateVars();
        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doAfterBody() {
        _currentIndex += _currentStep;
        _currentCount += 1;
        if (null != _varStatus) {
            if (_isFirst) {
                _propertyReplacementMap.put("first", Boolean.FALSE);
                _isFirst = false;
            }
            _isLast = (_currentIndex == _currentEnd);
            if (_isLast) {
                _propertyReplacementMap.put("last", _isLast);
            }
            _propertyReplacementMap.put("count", Integer.valueOf(_currentCount));
            _propertyReplacementMap.put("index", Integer.valueOf(_currentIndex));
        }
        if (_currentEnd < _currentIndex) {
            VariableMapper vm = pageContext.getELContext().getVariableMapper();
            if (_var != null) vm.setVariable(_var, _previousDeferredVar);
            if (_varStatus != null) vm.setVariable(_varStatus, _previousDeferredVarStatus);
            return SKIP_BODY;
        }
        _updateVars();
        return EVAL_BODY_AGAIN;
    }

    /**
   * Release state.
   */
    @Override
    public void release() {
        super.release();
        _begin = null;
        _end = null;
        _step = null;
        _items = null;
        _itemsValue = null;
        _var = null;
        _varStatus = null;
        _propertyReplacementMap = null;
        _previousDeferredVar = null;
        _previousDeferredVarStatus = null;
    }

    private void _updateVars() {
        VariableMapper vm = pageContext.getELContext().getVariableMapper();
        if (_var != null) {
            if (_items != null) {
                ValueExpression iterated = new IndexedValueExpression(_items, _currentIndex);
                vm.setVariable(_var, iterated);
            }
            Object items = _itemsValue;
            if (items != null) {
                Object item;
                if (items instanceof List) item = ((List) items).get(_currentIndex); else item = Array.get(items, _currentIndex);
                pageContext.setAttribute(_var, item);
            }
        }
        if (_varStatus != null) {
            pageContext.setAttribute(_varStatus, _propertyReplacementMap);
            ValueExpression constant = new Constants(new HashMap(_propertyReplacementMap));
            vm.setVariable(_varStatus, constant);
        }
    }

    private Integer _evaluateInteger(FacesContext context, ValueExpression ve) {
        if (ve == null) return null;
        Object val = ve.getValue(context.getELContext());
        if (val instanceof Integer) return (Integer) val; else if (val instanceof Number) return Integer.valueOf(((Number) val).intValue());
        return null;
    }

    private void _validateAttributes() throws JspTagException {
        FacesContext context = FacesContext.getCurrentInstance();
        _end = _evaluateInteger(context, _endVE);
        _begin = _evaluateInteger(context, _beginVE);
        _step = _evaluateInteger(context, _stepVE);
        if (null == _items) {
            if (null == _begin || null == _end) {
                throw new JspTagException("'begin' and 'end' should be specified if 'items' is not specified");
            }
        }
        if ((_var != null) && _var.equals(_varStatus)) {
            throw new JspTagException("'var' and 'varStatus' should not have the same value");
        }
    }

    private void _validateRangeAndStep() throws JspTagException {
        if (_currentBegin < 0) throw new JspTagException("'begin' < 0");
        if (_currentStep < 1) throw new JspTagException("'step' < 1");
    }

    private static class Constants extends ValueExpression implements Serializable {

        public Constants(Object o) {
            _o = o;
        }

        public Object getValue(ELContext context) {
            return _o;
        }

        public void setValue(ELContext context, Object value) {
            throw new PropertyNotWritableException();
        }

        public boolean isReadOnly(ELContext context) {
            return true;
        }

        public Class getType(ELContext context) {
            return _o.getClass();
        }

        public Class getExpectedType() {
            return _o.getClass();
        }

        public String getExpressionString() {
            return null;
        }

        public boolean equals(Object obj) {
            return obj == this;
        }

        public int hashCode() {
            return _o.hashCode();
        }

        public boolean isLiteralText() {
            return true;
        }

        private Object _o;
    }

    private int _currentBegin;

    private int _currentIndex;

    private int _currentEnd;

    private int _currentStep;

    private int _currentCount;

    private boolean _isFirst;

    private boolean _isLast;

    private ValueExpression _items;

    private Object _itemsValue;

    private ValueExpression _beginVE;

    private ValueExpression _endVE;

    private ValueExpression _stepVE;

    private Integer _begin;

    private Integer _end;

    private Integer _step;

    private String _var;

    private String _varStatus;

    private ValueExpression _previousDeferredVar;

    private ValueExpression _previousDeferredVarStatus;

    private Map<String, Object> _propertyReplacementMap;

    private static final TrinidadLogger _LOG = TrinidadLogger.createTrinidadLogger(ForEachTag.class);
}
