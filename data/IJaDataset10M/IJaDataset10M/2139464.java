package org.owasp.jxt.tag.jsp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.ValidationMessage;
import javax.servlet.jsp.tagext.VariableInfo;
import org.owasp.jxt.JxtExpr;
import org.owasp.jxt.JxtNode;
import org.owasp.jxt.TranslateContext;
import org.owasp.jxt.ValidationContext;
import org.owasp.jxt.webinf.AttributeDefinition;
import org.owasp.jxt.webinf.BodyContentType;
import org.owasp.jxt.webinf.TagDefinition;

/**
 * JspTagNode
 *
 * @author Jeffrey Ichnowski
 * @version $Revision: 8 $
 */
class JspTagNode extends JxtNode {

    TagDefinition _tagDef;

    Map<String, JxtExpr> _attrs = new TreeMap<String, JxtExpr>();

    List<VariableInfo> _vars = new ArrayList<VariableInfo>();

    transient String _tagVar;

    JspTagNode(TagDefinition tagDef) {
        super(tagDef.getName());
        _tagDef = tagDef;
    }

    @Override
    public void setAttribute(ValidationContext ctx, String name, String value) {
        AttributeDefinition attrDef = _tagDef.getAttribute(name);
        JxtExpr expr;
        if (attrDef == null) {
            expr = JxtExpr.createLiteral(value);
        } else {
            if (attrDef.isRtExprValue()) {
                expr = toExpr(ctx, value);
            } else {
                expr = JxtExpr.createLiteral(value);
            }
        }
        _attrs.put(name, expr);
    }

    private TagData getTagData() {
        Object[][] attrs = new Object[_attrs.size()][];
        int i = 0;
        for (Entry<String, JxtExpr> entry : _attrs.entrySet()) {
            JxtExpr expr = entry.getValue();
            Object value;
            if (expr.isLiteral()) {
                value = expr.getText();
            } else {
                value = TagData.REQUEST_TIME_VALUE;
            }
            attrs[i++] = new Object[] { entry.getKey(), value };
        }
        return new TagData(attrs);
    }

    @Override
    protected void validateSelf(ValidationContext ctx) {
        for (AttributeDefinition attrDef : _tagDef.getAttributes()) {
            if (attrDef.isRequired() && !_attrs.containsKey(attrDef.getName())) {
                ctx.error(this, "validation.attribute-required", attrDef.getName());
            }
        }
        for (Entry<String, JxtExpr> entry : _attrs.entrySet()) {
            String name = entry.getKey();
            JxtExpr value = entry.getValue();
            AttributeDefinition attrDef = _tagDef.getAttribute(name);
            if (null == attrDef) {
                ctx.error(this, "validation.attribute-unknown", name);
            } else if (value.isLiteral()) {
                try {
                    value.toCode(attrDef.getType());
                } catch (IllegalArgumentException e) {
                    ctx.error(this, "validation.attribute-type", name, attrDef.getType().getName());
                }
            }
        }
        if (_tagDef.getBodyContent() == BodyContentType.empty) {
            if (getFirstChild() != null) {
                ctx.error(this, "validation.content-empty", _tagDef.getName());
            }
        }
        _vars = new ArrayList<VariableInfo>();
        TagExtraInfo tei = _tagDef.getTagExtraInfo();
        if (tei != null) {
            TagData tagData = getTagData();
            ValidationMessage[] msgs = tei.validate(tagData);
            if (msgs != null) {
                for (ValidationMessage msg : msgs) {
                    ctx.error(this, "validation.tei-message", msg.getMessage());
                }
            }
            VariableInfo[] vars = tei.getVariableInfo(tagData);
            if (vars != null) {
                _vars.addAll(Arrays.asList(vars));
            }
        }
    }

    boolean isMethodized() {
        if (true) {
            return false;
        }
        if (getFirstChild() != null) {
            return false;
        }
        return true;
    }

    String methodName() {
        StringBuilder buf = new StringBuilder();
        buf.append("__jxtJspTag_");
        buf.append(_tagDef.getName());
        for (String attr : _attrs.keySet()) {
            buf.append('_').append(attr);
        }
        return buf.toString();
    }

    @Override
    public void translateDecl(TranslateContext ctx) {
        if (isMethodized()) {
            String methodName = methodName();
            String declKey = JspTagNode.class.getName() + "." + methodName;
            if (ctx.getAttribute(declKey) != null) {
                return;
            }
            ctx.setAttribute(declKey, Boolean.TRUE);
            StringBuilder sig = new StringBuilder();
            sig.append("private int ").append(methodName).append("(PageContext ctx, Tag parent");
            for (String attrName : _attrs.keySet()) {
                AttributeDefinition attrDef = _tagDef.getAttribute(attrName);
                sig.append(", ").append(attrDef.getType().getName()).append(attrName);
            }
            sig.append(") throws ServletException, IOException {");
            ctx.codeln(sig.toString());
            ctx.indent();
            if (true) {
                translateMain(ctx, "__jxtTag", true);
            }
            ctx.outdent();
            ctx.codeln("}");
        }
    }

    @Override
    public void translate(TranslateContext ctx) {
        _tagVar = ctx.generateVariableName("JspTag" + _tagDef.getName());
        VariableInfo[] vars = null;
        TagExtraInfo tei = _tagDef.getTagExtraInfo();
        if (tei != null) {
            TagData tagData = getTagData();
            vars = tei.getVariableInfo(tagData);
            if (vars != null && vars.length == 0) {
                vars = null;
            }
        }
        ctx.translateLocationComment(this);
        if (isMethodized()) {
            translateMethodCall(ctx);
        } else {
            translateMain(ctx, _tagVar, false);
        }
        ctx.codeln(_tagVar + " = null; // GC");
    }

    private void translateMethodCall(TranslateContext ctx) {
        StringBuilder call = new StringBuilder();
        call.append("if (").append(methodName()).append("(pageContext, null");
        for (JxtExpr value : _attrs.values()) {
            call.append(", ").append(value);
        }
        call.append(") == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) return;");
        ctx.codeln(call.toString());
    }

    private void declareVars(TranslateContext ctx, int scope) {
        if (_vars != null && !_vars.isEmpty()) {
            for (VariableInfo var : _vars) {
                if (var.getDeclare() && var.getScope() == scope) {
                    ctx.codeln(var.getClassName() + " " + var.getVarName() + " = null;");
                }
            }
        }
    }

    private void syncVars(TranslateContext ctx, int scope) {
        if (_vars != null && !_vars.isEmpty()) {
            for (VariableInfo var : _vars) {
                if (var.getScope() == scope) {
                    ctx.codeln(var.getVarName() + " = (" + var.getClassName() + ")pageContext.findAttribute(\"" + var.getVarName() + "\");");
                }
            }
        }
    }

    private String getParentVar() {
        for (JxtNode p = getParent(); p != null; p = p.getParent()) {
            if (p instanceof JspTagNode) {
                return ((JspTagNode) p)._tagVar;
            }
        }
        return "null";
    }

    private void translateMain(TranslateContext ctx, String tagVar, boolean methodBody) {
        ctx.codeln(_tagDef.getTagClass() + " " + tagVar + " = new " + _tagDef.getTagClass() + "();");
        ctx.codeln(tagVar + ".setPageContext(pageContext);");
        ctx.codeln(tagVar + ".setParent(" + getParentVar() + ");");
        Iterator<Entry<String, JxtExpr>> attrIter = _attrs.entrySet().iterator();
        while (attrIter.hasNext()) {
            Entry<String, JxtExpr> attr = attrIter.next();
            String name = attr.getKey();
            JxtExpr value = attr.getValue();
            AttributeDefinition attrDef = _tagDef.getAttribute(name);
            ctx.codeln(tagVar + ".set" + Character.toUpperCase(name.charAt(0)) + name.substring(1) + "(" + (methodBody ? name : value.toCode(attrDef.getType())) + ");");
        }
        declareVars(ctx, VariableInfo.AT_BEGIN);
        if (_tagDef.isTryCatchFinally()) {
            translateTryCatchFinallyTag(ctx, tagVar);
        } else {
            translateTag(ctx, tagVar);
        }
        declareVars(ctx, VariableInfo.AT_END);
        syncVars(ctx, VariableInfo.AT_BEGIN);
        syncVars(ctx, VariableInfo.AT_END);
    }

    private void translateTryCatchFinallyTag(TranslateContext ctx, String tagVar) {
        String exVar = ctx.generateVariableName("JspEx");
        ctx.codeln("try {");
        ctx.indent();
        if (true) {
            translateTag(ctx, tagVar);
        }
        ctx.outdent();
        ctx.codeln("} catch (Throwable " + exVar + ") {");
        ctx.indent();
        if (true) {
            ctx.codeln(tagVar + ".doCatch(" + exVar + ");");
        }
        ctx.outdent();
        ctx.codeln("} finally {");
        ctx.indent();
        if (true) {
            ctx.codeln(tagVar + ".doFinally()");
        }
        ctx.outdent();
        ctx.codeln("}");
    }

    private void translateTag(TranslateContext ctx, String tagVar) {
        String startVar = ctx.generateVariableName("JspStart");
        String endVar = ctx.generateVariableName("JspEnd");
        ctx.codeln("int " + startVar + " = " + tagVar + ".doStartTag();");
        if (!_tagDef.isEmpty()) {
            ctx.codeln("if (" + startVar + " != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {");
            ctx.indent();
            if (true) {
                declareVars(ctx, VariableInfo.NESTED);
                if (_tagDef.isBodyTag()) {
                    translateBodyTag(ctx, tagVar, startVar);
                } else if (_tagDef.isIterationTag()) {
                    translateIterationLoop(ctx, tagVar);
                } else {
                    translateNested(ctx);
                }
            }
            ctx.outdent();
            ctx.codeln("}");
        }
        if (isMethodized()) {
            ctx.codeln("return " + tagVar + ".doEndTag();");
        } else {
            ctx.codeln("int " + endVar + " = " + tagVar + ".doEndTag();");
            ctx.codeln("if (" + endVar + " == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {");
            ctx.indent();
            if (true) {
                ctx.codeln("return;");
            }
            ctx.outdent();
            ctx.codeln("}");
        }
    }

    private void translateBodyTag(TranslateContext ctx, String tagVar, String startVar) {
        ctx.codeln("if (" + startVar + " != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {");
        ctx.indent();
        if (true) {
            ctx.codeln("out = pageContext.pushBody();");
            ctx.codeln(tagVar + ".setBodyContent((javax.servlet.jsp.tagext.BodyContent)out);");
            ctx.codeln(tagVar + ".doInitBody();");
        }
        ctx.outdent();
        ctx.codeln("}");
        translateIterationLoop(ctx, tagVar);
        ctx.codeln("if (" + startVar + " != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {");
        ctx.indent();
        if (true) {
            ctx.codeln("out = pageContext.popBody();");
        }
        ctx.outdent();
        ctx.codeln("}");
    }

    private void translateIterationLoop(TranslateContext ctx, String tagVar) {
        String afterBodyVar = ctx.generateVariableName("JspAfterBody");
        ctx.codeln("for (;;) {");
        ctx.indent();
        if (true) {
            translateNested(ctx);
            ctx.codeln("int " + afterBodyVar + " = " + tagVar + ".doAfterBody();");
            syncVars(ctx, VariableInfo.AT_BEGIN);
            syncVars(ctx, VariableInfo.NESTED);
            ctx.codeln("if (" + afterBodyVar + " != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN) {");
            ctx.indent();
            if (true) {
                ctx.codeln("break;");
            }
            ctx.outdent();
            ctx.codeln("}");
        }
        ctx.outdent();
        ctx.codeln("}");
    }

    private void translateNested(TranslateContext ctx) {
        syncVars(ctx, VariableInfo.AT_BEGIN);
        syncVars(ctx, VariableInfo.NESTED);
        translateChildren(ctx);
    }
}
