package br.com.linkcom.neo.rtf;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;
import br.com.linkcom.neo.exception.RTFException;

public class RTFGeneratorImpl implements RTFGenerator {

    private RTFNameResolver nameResolver;

    public RTFGeneratorImpl(RTFNameResolver nameResolver) {
        this.nameResolver = nameResolver;
    }

    public byte[] generate(RTF rtf) {
        ByteArrayOutputStream out;
        try {
            Map<String, String> parameterMap = rtf.getParameterMap();
            if (parameterMap == null) {
                throw new NullPointerException("RTF n�o possui parameterMap");
            }
            InputStream in = nameResolver.resolveName(rtf.getName());
            out = new ByteArrayOutputStream();
            int i = 0;
            boolean inTag = false;
            StringBuilder currentTag = new StringBuilder();
            while ((i = in.read()) != -1) {
                if (i == '<') {
                    inTag = true;
                    continue;
                }
                if (i == '>') {
                    inTag = false;
                    String param = currentTag.toString();
                    String value = parameterMap.get(param);
                    if (value != null) {
                        byte[] bytes = value.getBytes();
                        out.write(bytes);
                    }
                    currentTag = new StringBuilder();
                    continue;
                }
                if (inTag) {
                    currentTag.append((char) i);
                } else {
                    out.write(i);
                }
            }
        } catch (Exception e) {
            throw new RTFException(e);
        }
        return out.toByteArray();
    }
}
