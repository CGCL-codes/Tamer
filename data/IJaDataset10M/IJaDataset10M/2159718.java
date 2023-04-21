package de.huxhorn.lilith.data.access.protobuf;

import de.huxhorn.lilith.data.access.AccessEvent;
import de.huxhorn.lilith.data.access.protobuf.generated.AccessProto;
import de.huxhorn.lilith.data.eventsource.LoggerContext;
import de.huxhorn.sulky.codec.Encoder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

public class AccessEventProtobufEncoder implements Encoder<AccessEvent> {

    private boolean compressing;

    public AccessEventProtobufEncoder(boolean compressing) {
        this.compressing = compressing;
    }

    public boolean isCompressing() {
        return compressing;
    }

    public void setCompressing(boolean compressing) {
        this.compressing = compressing;
    }

    public byte[] encode(AccessEvent event) {
        AccessProto.AccessEvent converted = convert(event);
        if (converted == null) {
            return null;
        }
        if (!compressing) {
            return converted.toByteArray();
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gos;
        try {
            gos = new GZIPOutputStream(out);
            converted.writeTo(gos);
            gos.flush();
            gos.close();
            return out.toByteArray();
        } catch (IOException e) {
        }
        return null;
    }

    public static AccessProto.AccessEvent convert(AccessEvent event) {
        if (event == null) {
            return null;
        }
        AccessProto.AccessEvent.Builder eventBuilder = AccessProto.AccessEvent.newBuilder();
        {
            String method = event.getMethod();
            if (method != null) {
                eventBuilder.setMethod(method);
            }
        }
        {
            String protocol = event.getProtocol();
            if (protocol != null) {
                eventBuilder.setProtocol(protocol);
            }
        }
        {
            String address = event.getRemoteAddress();
            if (address != null) {
                eventBuilder.setRemoteAddress(address);
            }
        }
        {
            String host = event.getRemoteHost();
            if (host != null) {
                eventBuilder.setRemoteHost(host);
            }
        }
        {
            String user = event.getRemoteUser();
            if (user != null) {
                eventBuilder.setRemoteUser(user);
            }
        }
        {
            String uri = event.getRequestURI();
            if (uri != null) {
                eventBuilder.setRequestUri(uri);
            }
        }
        {
            String url = event.getRequestURL();
            if (url != null) {
                eventBuilder.setRequestUrl(url);
            }
        }
        {
            String url = event.getRequestURL();
            if (url != null) {
                eventBuilder.setRequestUrl(url);
            }
        }
        {
            String name = event.getServerName();
            if (name != null) {
                eventBuilder.setServerName(name);
            }
        }
        {
            Long ts = event.getTimeStamp();
            if (ts != null) {
                eventBuilder.setTimeStamp(ts);
            }
        }
        {
            int port = event.getLocalPort();
            eventBuilder.setLocalPort(port);
        }
        {
            int status = event.getStatusCode();
            eventBuilder.setStatusCode(status);
        }
        {
            AccessProto.StringMap data = convertStringMap(event.getRequestHeaders());
            if (data != null) {
                eventBuilder.setRequestHeaders(data);
            }
        }
        {
            AccessProto.StringMap data = convertStringMap(event.getResponseHeaders());
            if (data != null) {
                eventBuilder.setResponseHeaders(data);
            }
        }
        {
            AccessProto.StringArrayMap data = convertStringArrayMap(event.getRequestParameters());
            if (data != null) {
                eventBuilder.setRequestParameters(data);
            }
        }
        {
            LoggerContext context = event.getLoggerContext();
            if (context != null) {
                eventBuilder.setLoggerContext(convert(context));
            }
        }
        return eventBuilder.build();
    }

    public static AccessProto.LoggerContext convert(LoggerContext context) {
        if (context == null) {
            return null;
        }
        AccessProto.LoggerContext.Builder builder = AccessProto.LoggerContext.newBuilder();
        {
            String name = context.getName();
            if (name != null) {
                builder.setName(name);
            }
        }
        {
            Long birthTime = context.getBirthTime();
            if (birthTime != null) {
                builder.setBirthTime(birthTime);
            }
        }
        {
            Map<String, String> map = context.getProperties();
            if (map != null && map.size() > 0) {
                builder.setProperties(convertStringMap(map));
            }
        }
        return builder.build();
    }

    public static AccessProto.StringMap convertStringMap(Map<String, String> data) {
        if (data == null) {
            return null;
        }
        AccessProto.StringMap.Builder builder = AccessProto.StringMap.newBuilder();
        for (Map.Entry<String, String> current : data.entrySet()) {
            AccessProto.StringMapEntry.Builder entryBuilder = AccessProto.StringMapEntry.newBuilder().setKey(current.getKey());
            String value = current.getValue();
            if (value != null) {
                entryBuilder.setValue(value);
            }
            builder.addEntry(entryBuilder.build());
        }
        return builder.build();
    }

    public static AccessProto.StringArrayMap convertStringArrayMap(Map<String, String[]> data) {
        if (data == null) {
            return null;
        }
        AccessProto.StringArrayMap.Builder builder = AccessProto.StringArrayMap.newBuilder();
        for (Map.Entry<String, String[]> current : data.entrySet()) {
            AccessProto.StringArrayMapEntry.Builder entryBuilder = AccessProto.StringArrayMapEntry.newBuilder().setKey(current.getKey());
            String[] value = current.getValue();
            if (value != null && value.length > 0) {
                for (String cur : value) {
                    AccessProto.StringArrayValue.Builder valBuilder = AccessProto.StringArrayValue.newBuilder();
                    if (cur != null) {
                        valBuilder.setValue(cur);
                    }
                    entryBuilder.addValue(valBuilder.build());
                }
            }
            builder.addEntry(entryBuilder.build());
        }
        return builder.build();
    }
}
