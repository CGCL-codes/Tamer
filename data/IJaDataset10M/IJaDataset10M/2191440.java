package anyware.desktop.support;

import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.AbstractFactoryBean;

/**
 * Spring {@link FactoryBean} for creating a {@link RAMDirectory} instance.
 * 
 * @author keke
 * 
 */
public class RAMDirectoryFactoryBean extends AbstractFactoryBean {

    private FSDirectory fsDirectory;

    @Override
    public Class<?> getObjectType() {
        return RAMDirectory.class;
    }

    public void setFsDirectory(final FSDirectory fsDirectory) {
        this.fsDirectory = fsDirectory;
    }

    @Override
    protected Object createInstance() throws Exception {
        if (fsDirectory != null) {
            return new RAMDirectory(fsDirectory);
        }
        return new RAMDirectory();
    }
}
