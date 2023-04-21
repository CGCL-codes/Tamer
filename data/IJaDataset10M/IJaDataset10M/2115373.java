package net.sf.balm.spring.resource;

import java.io.IOException;
import org.springframework.core.type.classreading.MetadataReader;

/**
 * 资源处理接口，接受一个MetadataReader，并根据用户需要进行处理
 * 
 * @author dz
 */
public interface MetadataProcessor {

    public static final MetadataProcessor DUMMY = new MetadataProcessor() {

        public void process(MetadataReader reader) throws IOException {
        }
    };

    /**
     * @param file
     * @throws IOException
     */
    public void process(MetadataReader reader) throws IOException;
}
