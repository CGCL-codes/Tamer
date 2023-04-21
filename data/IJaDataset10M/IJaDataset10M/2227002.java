package com.aaronprj.common.web.resource;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import com.aaronprj.common.web.uivo.BaseEntity;
import com.sun.jersey.api.core.InjectParam;

public class ResourceServices {

    /**
	 * @param <T> 
	 * 
	 */
    @InjectParam
    protected ObjectMapper mapper;

    @Context
    UriInfo uriInfo;

    @Context
    HttpServletRequest request;

    public String getWebApplicationRealPath(String vfolder) {
        System.out.println(request.getContextPath().toString());
        System.out.println(request.getLocalAddr().toString());
        System.out.println("request.getPathInfo():" + request.getPathInfo().toString());
        System.out.println("request.getPathTranslated():" + request.getPathTranslated().toString());
        System.out.println("request.getRealPath():" + request.getRealPath("").toString());
        System.out.println(request.getRemoteAddr().toString());
        System.out.println(request.getServletPath().toString());
        System.out.println(request.getRequestURL().toString());
        System.out.println("uriInfo.getPath:" + uriInfo.getPath().toString());
        System.out.println("uriInfo.getAbsolutePath():" + uriInfo.getAbsolutePath().toString());
        System.out.println("uriInfo.getBaseUri()" + uriInfo.getBaseUri().toString());
        String vpath = request.getPathTranslated().toString();
        vpath = vpath.substring(0, vpath.indexOf(request.getPathInfo().toString()));
        System.out.println("edited path:::" + vpath);
        vpath = vpath + File.separator + vfolder + File.separator;
        System.out.println("added folder path:::" + vpath);
        return vpath;
    }

    public String writeValueAsString(Object entity) {
        try {
            if (mapper == null) mapper = new ObjectMapper();
            return mapper.writeValueAsString(entity);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return generateFailJSON("Error", "Unknow Error!");
    }

    /**
	 * @param <T> 
	 * 
	 */
    public <T> T toObject(String content, Class<T> valueType) {
        try {
            return mapper.readValue(content, valueType);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String generateFailJSON(String msgcode, String message) {
        return "{\"id\":1105,\"success\":false,\"msgCode\":\"" + msgcode + "\",\"msgDiscription\":\"" + message + "\"}";
    }

    public static <T extends BaseEntity> T generateResult(Class<T> classType, boolean success, String msgCode, String msgDiscription) {
        return generateResult(classType, success, msgCode, msgDiscription, "", "");
    }

    public static <T extends BaseEntity> T generateResult(Class<T> classType, boolean success, String msgCode, String msgDiscription, String sessionId) {
        return generateResult(classType, success, msgCode, msgDiscription, sessionId, "");
    }

    public static <T extends BaseEntity> T generateResult(Class<T> classType, boolean success, String msgCode, String msgDiscription, String sessionId, String orderNo) {
        T un;
        try {
            un = classType.newInstance();
            un.setSuccess(success);
            un.setMsgCode(msgCode);
            un.setMsgDiscription(msgDiscription);
            un.setSessionId(sessionId);
            un.setOrderNo(orderNo);
            return un;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * Set State field from value.
	 * 
	 * @param <T>
	 * @param myState
	 * @param name
	 * @param value
	 */
    public static <T extends Object> void setStateField(T testobj, String name, String value) {
        if (value == null || value.length() == 0) return;
        List<Field> allFields = new ArrayList<Field>();
        allFields.addAll(Arrays.asList(testobj.getClass().getDeclaredFields()));
        allFields.addAll(Arrays.asList(testobj.getClass().getSuperclass().getDeclaredFields()));
        for (Field field : allFields) {
            if (field.getName().equals(name)) {
                char[] chars = field.getName().toCharArray();
                chars[0] = Character.toUpperCase(chars[0]);
                Class<?> fieldType = field.getType();
                Object obj;
                if (fieldType == int.class) {
                    obj = Integer.valueOf(value);
                } else if (fieldType == long.class) {
                    obj = Long.valueOf(value);
                } else if (fieldType == double.class) {
                    obj = Double.valueOf(value);
                } else if (fieldType == String.class) {
                    obj = String.valueOf(value);
                } else {
                    obj = value;
                }
                Method setMethod = null;
                try {
                    setMethod = testobj.getClass().getDeclaredMethod("set" + new String(chars), fieldType);
                } catch (NoSuchMethodException ex) {
                    try {
                        setMethod = testobj.getClass().getSuperclass().getDeclaredMethod("set" + new String(chars), fieldType);
                    } catch (NoSuchMethodException ef) {
                        ef.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (setMethod != null) {
                    try {
                        setMethod.invoke(testobj, obj);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
        }
    }

    public UUID getSessionId() {
        return UUID.randomUUID();
    }
}
