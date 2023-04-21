package org.soybeanMilk.web.os;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.soybeanMilk.SoybeanMilkUtils;
import org.soybeanMilk.core.Constants;
import org.soybeanMilk.core.ObjectSourceException;
import org.soybeanMilk.core.bean.Converter;
import org.soybeanMilk.core.bean.GenericConverter;
import org.soybeanMilk.core.os.ConvertableObjectSource;
import org.soybeanMilk.web.WebConstants;
import org.soybeanMilk.web.WebObjectSource;
import org.soybeanMilk.web.bean.ParamPropertyMap;
import org.soybeanMilk.web.bean.ParamValue;

/**
 * 默认的{@linkplain WebObjectSource WEB对象源}实现。
 * <br>
 * 传递给它的关键字会被理解为由两个部分组成：“[scope].[keyInScope]”，其中
 * “[scope]”表示作用域，“[keyInScope]”则是真正的该作用域下的关键字。
 * <br>
 * 它目前所支持的关键字格式及其说明如下：
 * <ul>
 * 	<li>
 *   set
 *   <ul>
 *  	<li>
 *  		<span class="tagValue">keyInScope</span> <br/>
 *  		结果将以“<span class="var">keyInScope</span>”关键字被保存到“<span class="var">request</span>”作用域中
 *  	</li>
 *  	<li>
 *  		<span class="tagValue">request.keyInScope</span> <br/>
 *  		同上
 *  	</li>
 *  	<li>
 *  		<span class="tagValue">session.keyInScope</span> <br/>
 *  		结果将以“<span class="var">keyInScope</span>”关键字被保存到“<span class="var">session</span>”作用域中
 *  	</li>
 *  	<li>
 *  		<span class="tagValue">application.keyInScope</span> <br/>
 *  		结果将以“<span class="var">keyInScope</span>”关键字被保存到“<span class="var">application</span>”作用域中
 *  	</li>
 *   </ul>
 *  </li>
 *  <li>
 *  	get
 *  	<ul>
 *  	<li>
 *  		<span class="tagValue">param</span> <br/>
 *  		整个请求参数映射表。如果目标类型是<span class="var">java.util.Map</span>，
 *  		那么它不会做任何处理而直接返回整个参数映射表；如果是其他类型，它会首先将此映射表转换为这个类型的对象，然后返回此对象。
 *  	</li>
 *  	<li>
 *  		<span class="tagValue">keyInScope</span> <br/>
 *  		请求参数映射表中以“<span class="var">keyInScope</span>”开头的请求参数。
 *  		如果这个参数有明确的值，它将对这个值进行类型转换（需要的话），然后返回转换后的对象；否则，就根据“<span class="var">keyInScope</span>”来对参数映射表进行过滤，
 *  		产生一个新的映射表（它的主键是原始关键字“<span class="var">keyInScope.</span>”之后的部分，比如由“<span class="var">beanName.propertyName</span>”变为“<span class="var">propertyName</span>”），
 *  		然后，与上面提到的一样，根据目标类型直接返回这个新映射表或者返回转换后的对象。
 *  	</li>
 *  	<li>
 *  		<span class="tagValue">param.keyInScope</span> <br/>
 *  		同上。
 *  	</li>
 *  	<li>
 *  		<span class="tagValue">request</span> <br/>
 *  		请求HttpServletRequest对象。框架本身并没有提供它的转换器，如果目标类型不是“<span class="var">HttpServletRequest</span>”，
 *  		那么你需要为此对象源的{@linkplain GenericConverter 通用转换器}添加“<span class="var">javax.servlet.http.HttpServletRequest</span>”到目标类型的辅助{@linkplain Converter 转换器}。
 *  	</li>
 *  	<li>
 *  		<span class="tagValue">request.keyInScope</span> <br/>
 *  		请求属性中的“<span class="var">keyInScope</span>”关键字对应的对象。如果目标类型与此对象不一致，框架将尝试执行类型转换。
 *  	</li>
 *  	<li>
 *  		<span class="tagValue">session</span> <br/>
 *  		会话HttpSession对象。框架本身并没有提供它的转换器，如果目标类型不是“<span class="var">HttpSession</span>”，
 *  		那么你需要为此对象源的{@linkplain GenericConverter 通用转换器}添加“<span class="var">javax.servlet.http.HttpSession</span>”到目标类型的辅助{@linkplain Converter 转换器}。
 *  	</li>
 *  	<li>
 *  		<span class="tagValue">session.keyInScope</span> <br/>
 *  		会话属性中的“<span class="var">keyInScope</span>”关键字对应的对象。如果目标类型与此对象不一致，框架将尝试执行类型转换。
 *  	</li>
 *  	<li>
 *  		<span class="tagValue">application</span> <br/>
 *  		应用ServletContext对象。如果目标类型不是“<span class="var">ServletContext</span>”，
 *  		那么你需要为此对象源的{@linkplain GenericConverter 通用转换器}添加“<span class="var">javax.servlet.ServletContext</span>”到目标类型的辅助{@linkplain Converter 转换器}。
 *  	</li>
 *  	<li>
 *  		<span class="tagValue">application.keyInScope</span> <br/>
 *  		应用属性中的“<span class="var">keyInScope</span>”关键字对应的对象。如果目标类型与此对象不一致，框架将尝试执行类型转换。
 *  	</li>
 *  	<li>
 *  		<span class="tagValue">response</span> <br/>
 *  		回应HttpServletResponse对象。如果目标类型不是“<span class="var">HttpServletResponse</span>”，
 *  		那么你需要为此对象源的{@linkplain GenericConverter 通用转换器}添加“<span class="var">javax.servlet.http.HttpServletResponse</span>”到目标类型辅助{@linkplain Converter 转换器}。
 *  	</li>
 *  	<li>
 *  		<span class="tagValue">objectSource</span> <br/>
 *  		当前的{@linkplain DefaultWebObjectSource}对象，你可以使用它来获取所有servlet对象。如果目标类型不是“<span class="var">WebObjectSource</span>”，
 *  		那么你需要为此对象源的{@linkplain GenericConverter 通用转换器}添加“<span class="var">org.soybeanMilk.web.os.WebObjectSource</span>”到目标类型辅助{@linkplain Converter 转换器}。
 *  	</li>
 *   </ul>
 *  </li>
 * </ul>
 * <br>
 * 另外，如果“request”、“session”、“application”作用域的“[keyInScope]”中包含访问符“.”，比如“request.yourBean.property”，
 * 它会认为你是想要取得或设置“request”作用域内“yourBean”对象的“property”属性，并按此处理（如果“yourBean”对象存在的话）。
 * <br>
 * 实际上，你在配置文件中定义的&lt;arg&gt;关键字的格式就是由这个类决定的。
 * @author earthAngry@gmail.com
 * @date 2010-7-19
 */
public class DefaultWebObjectSource extends ConvertableObjectSource implements WebObjectSource {

    private static Log log = LogFactory.getLog(DefaultWebObjectSource.class);

    private HttpServletRequest request;

    private HttpServletResponse response;

    private ServletContext application;

    public DefaultWebObjectSource() {
        super();
    }

    public DefaultWebObjectSource(HttpServletRequest request, HttpServletResponse response, ServletContext application) {
        this(request, response, application, null);
    }

    public DefaultWebObjectSource(HttpServletRequest request, HttpServletResponse response, ServletContext application, GenericConverter genericConverter) {
        super();
        this.request = request;
        this.response = response;
        this.application = application;
        super.setGenericConverter(genericConverter);
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    /**
	 * 设置当前{@linkplain HttpServletRequest 请求}对象
	 * @param request
	 * @date 2011-12-11
	 */
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    /**
	 * 设置当前{@linkplain HttpServletResponse 回应}对象
	 * @param response
	 * @date 2011-12-11
	 */
    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public ServletContext getApplication() {
        return application;
    }

    /**
	 * 设置{@linkplain ServletContext Servlet语境}对象
	 * @param application
	 * @date 2011-12-11
	 */
    public void setApplication(ServletContext application) {
        this.application = application;
    }

    @SuppressWarnings("unchecked")
    public Object get(Serializable key, Type expectType) {
        Object data = null;
        String strKey = (String) key;
        if (strKey == null) throw new ObjectSourceException("[key] must not be null.");
        String[] scopedKey = SoybeanMilkUtils.splitByFirstAccessor(strKey);
        String scope = scopedKey[0];
        String subKey = scopedKey.length == 1 ? null : scopedKey[1];
        if (WebConstants.Scope.PARAM.equalsIgnoreCase(scope)) {
            data = convertParameterMap(getRequest().getParameterMap(), subKey, expectType);
        } else if (WebConstants.Scope.REQUEST.equalsIgnoreCase(scope)) {
            data = convertServletObjectAttribute(getRequest(), subKey, expectType);
        } else if (WebConstants.Scope.SESSION.equalsIgnoreCase(scope)) {
            data = convertServletObjectAttribute(getRequest().getSession(), subKey, expectType);
        } else if (WebConstants.Scope.APPLICATION.equalsIgnoreCase(scope)) {
            data = convertServletObjectAttribute(getApplication(), subKey, expectType);
        } else if (WebConstants.Scope.RESPONSE.equalsIgnoreCase(scope)) {
            data = convertServletObjectAttribute(getResponse(), subKey, expectType);
        } else if (WebConstants.Scope.OBJECT_SOURCE.equalsIgnoreCase(scope)) {
            if (subKey != null) throw new ObjectSourceException("invalide key '" + strKey + "', get object from '" + WebConstants.Scope.OBJECT_SOURCE + "' scope is not supported");
            if (expectType == null || SoybeanMilkUtils.isInstanceOf(this, expectType)) data = this; else {
                data = getConverterNotNull(DefaultWebObjectSource.class, expectType).convert(this, expectType);
            }
        } else if (scopedKey.length == 1) {
            data = convertParameterMap(getRequest().getParameterMap(), strKey, expectType);
            scope = WebConstants.Scope.PARAM;
            subKey = strKey;
        } else data = getObjectFromUnknownScope(scope, subKey, expectType);
        if (log.isDebugEnabled()) log.debug("get '" + data + "' from scope '" + scope + "' with key '" + subKey + "'");
        return data;
    }

    public void set(Serializable key, Object obj) {
        String strKey = (String) key;
        if (strKey == null) throw new IllegalArgumentException("[key] must not be null");
        String[] scopedKey = SoybeanMilkUtils.splitByFirstAccessor(strKey);
        String scope = scopedKey.length == 1 ? WebConstants.Scope.REQUEST : scopedKey[0];
        String subKey = scopedKey.length == 1 ? scopedKey[0] : scopedKey[1];
        if (WebConstants.Scope.REQUEST.equalsIgnoreCase(scope)) {
            setAttributeByKeyExpression(getRequest(), subKey, obj);
        } else if (WebConstants.Scope.SESSION.equalsIgnoreCase(scope)) {
            setAttributeByKeyExpression(getRequest().getSession(), subKey, obj);
        } else if (WebConstants.Scope.APPLICATION.equalsIgnoreCase(scope)) {
            setAttributeByKeyExpression(getApplication(), subKey, obj);
        } else if (WebConstants.Scope.PARAM.equalsIgnoreCase(scope)) {
            throw new ObjectSourceException("'" + key + "' is invalid, you can not save object into '" + WebConstants.Scope.PARAM + "' scope");
        } else if (WebConstants.Scope.RESPONSE.equalsIgnoreCase(scope)) {
            throw new ObjectSourceException("'" + key + "' is not valid, you can not save object into '" + WebConstants.Scope.RESPONSE + "' scope");
        } else setObjectToUnknownScope(scope, subKey, obj);
        if (log.isDebugEnabled()) log.debug("save '" + obj + "' into scope '" + scope + "' with key '" + subKey + "'");
    }

    /**
	 * 从无法识别的作用域中获取对象。
	 * @param scope 作用域
	 * @param keyInScope 此作用域中的对象关键字
	 * @param expectType
	 * @return
	 * @date 2011-2-22
	 */
    protected Object getObjectFromUnknownScope(String scope, String keyInScope, Type expectType) {
        throw new ObjectSourceException("key '" + (scope + Constants.ACCESSOR + keyInScope) + "' is invalid, get object from scope '" + scope + "' is not supported");
    }

    /**
	 * 将对象保存到无法识别的作用域中。
	 * @param scope 作用域
	 * @param keyInScope 此作用域中的保存关键字
	 * @param value
	 * @date 2011-2-22
	 */
    protected void setObjectToUnknownScope(String scope, String keyInScope, Object value) {
        throw new ObjectSourceException("key '" + (scope + Constants.ACCESSOR + keyInScope) + "' is invalid, set object into scope '" + scope + "' is not supported");
    }

    /**
	 * 转换Servlet对象的属性对象，如果<code>keyExpression</code>为空，Servlet对象本身将被用于转换。
	 * @param servletObj
	 * @param keyExpression 关键字表达式，比如“myobj”、“myobj.myProperty”
	 * @param targetType
	 * @return
	 * @date 2011-2-22
	 */
    protected Object convertServletObjectAttribute(Object servletObj, String keyExpression, Type targetType) {
        Object re = null;
        if (keyExpression == null || keyExpression.length() == 0) {
            if (targetType == null || SoybeanMilkUtils.isInstanceOf(servletObj, targetType)) re = servletObj; else {
                Type srcType = getServletObjectType(servletObj);
                re = getConverterNotNull(srcType, targetType).convert(servletObj, targetType);
            }
        } else {
            String[] keyWithProperty = SoybeanMilkUtils.splitByFirstAccessor(keyExpression);
            re = getServletObjAttribute(servletObj, keyWithProperty[0]);
            if (keyWithProperty.length == 1) re = getGenericConverter().convert(re, targetType); else {
                if (re == null) re = getGenericConverter().convert(getServletObjAttribute(servletObj, keyExpression), targetType); else re = getGenericConverter().getProperty(re, keyWithProperty[1], targetType);
            }
        }
        return re;
    }

    /**
	 * 将对象保存到servlet对象作用域内。
	 * @param servletObj
	 * @param keyExpression 关键字表达式，比如“yourBean”、“yourBean.property”
	 * @param obj
	 * @date 2010-12-30
	 */
    protected void setAttributeByKeyExpression(Object servletObj, String keyExpression, Object obj) {
        String[] objKeyWithProperty = SoybeanMilkUtils.splitByFirstAccessor(keyExpression);
        if (objKeyWithProperty.length == 2) {
            Object data = getServletObjAttribute(servletObj, objKeyWithProperty[0]);
            if (data != null) getGenericConverter().setProperty(data, objKeyWithProperty[1], obj); else setServletObjAttribute(servletObj, keyExpression, obj);
        } else setServletObjAttribute(servletObj, keyExpression, obj);
    }

    /**
	 * 将对象保存到servlet对象作用域内。
	 * @param servletObj
	 * @param key
	 * @param value
	 * @date 2010-12-30
	 */
    protected void setServletObjAttribute(Object servletObj, String key, Object value) {
        if (servletObj instanceof HttpServletRequest) ((HttpServletRequest) servletObj).setAttribute(key, value); else if (servletObj instanceof HttpSession) ((HttpSession) servletObj).setAttribute(key, value); else if (servletObj instanceof ServletContext) ((ServletContext) servletObj).setAttribute(key, value); else throw new ObjectSourceException("can not set attribute to servlet object '" + servletObj + "'");
    }

    /**
	 * 从servlet对象作用域内取得对象。
	 * @param servletObj
	 * @param key
	 * @param value
	 * @date 2010-12-30
	 */
    protected Object getServletObjAttribute(Object servletObj, String key) {
        if (servletObj instanceof HttpServletRequest) return ((HttpServletRequest) servletObj).getAttribute(key); else if (servletObj instanceof HttpSession) return ((HttpSession) servletObj).getAttribute(key); else if (servletObj instanceof ServletContext) return ((ServletContext) servletObj).getAttribute(key); else throw new ObjectSourceException("can not get attribute from servlet object '" + servletObj + "'");
    }

    /**
	 * 转换请求参数映射表<br>
	 * 如果<code>paramKeyFilter</code>是一个明确的关键字（映射表中有该关键字的值），它将直接根据该关键字的值来转换；<br>
	 * 如果<code>paramKeyFilter</code>是<code>null</code>，那么它将使用原始的请求参数映射表来进行转换；<br>
	 * 否则，它会根据<code>paramKeyFilter</code>来对参数映射表进行过滤，产生一个新的映射表（它的关键字将会被替换为原始关键字的“<code>[paramKeyFilter]</code>.”之后的部分，比如由“<code>beanName.propertyName</code>”变为“<code>propertyName</code>”），
	 * 然后使用它进行转换。
	 * 
	 * @param paramMap 参数映射表
	 * @param paramKeyFilter 筛选器，只有以此筛选器开头的参数关键字才会被转换，如果为null，则表明不做筛选
	 * @param targetType 目标类型
	 * @return
	 */
    protected Object convertParameterMap(Map<String, ?> paramMap, String paramKeyFilter, Type targetType) {
        Object result = null;
        if (targetType == null) result = paramMap; else {
            Object explicitValue = paramMap.get(paramKeyFilter);
            if (explicitValue != null) {
                result = new ParamValue(paramKeyFilter, explicitValue);
            } else if (SoybeanMilkUtils.isClassType(targetType) && SoybeanMilkUtils.narrowToClassType(targetType).isPrimitive()) {
                result = new ParamValue(paramKeyFilter, explicitValue);
            } else {
                ParamPropertyMap ppm = new ParamPropertyMap(null, paramKeyFilter);
                ppm.filter(paramMap);
                result = ppm;
            }
        }
        result = getGenericConverter().convert(result, targetType);
        return result;
    }

    /**
	 * 取得Servlet对象的标准类型
	 * @param servletObject
	 * @return
	 * @date 2011-2-22
	 */
    protected Class<?> getServletObjectType(Object servletObject) {
        Class<?> type = null;
        if (servletObject instanceof HttpServletRequest) type = HttpServletRequest.class; else if (servletObject instanceof HttpSession) type = HttpSession.class; else if (servletObject instanceof HttpServletResponse) type = HttpServletResponse.class; else if (servletObject instanceof ServletContext) type = ServletContext.class; else throw new ObjectSourceException("unknown servlet object '" + servletObject.getClass().getName() + "'");
        return type;
    }

    /**
	 * 查找转换器，返回结果不会为<code>null</code>
	 * @param sourceType
	 * @param targetType
	 * @return
	 * @date 2011-4-14
	 */
    protected Converter getConverterNotNull(Type sourceType, Type targetType) {
        GenericConverter genericConverter = getGenericConverter();
        Converter cvt = (genericConverter == null ? null : genericConverter.getConverter(sourceType, targetType));
        if (cvt == null) throw new NullPointerException("no Converter defined for converting '" + sourceType + "' to '" + targetType + "'");
        return cvt;
    }
}
