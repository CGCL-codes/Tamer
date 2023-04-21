package net.sf.appomatox.gui.diagramm.plots;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.io.ObjectStreamException;
import net.sf.appomatox.gui.diagramm.DiagrammMasse;
import org.codehaus.groovy.control.CompilationFailedException;
import com.thoughtworks.xstream.annotations.XStreamAlias;

public class PlottbaresGroovy extends Plottbar {

    @XStreamAlias("code")
    private File m_Code;

    private transient GroovyObject m_GroovyObject = null;

    public PlottbaresGroovy(File code) throws CompilationFailedException, InstantiationException, IllegalAccessException, IOException {
        m_Code = code;
        initCode();
    }

    @Override
    public void paint(Graphics2D g, DiagrammMasse dm) {
        if (m_GroovyObject != null) {
            ((Plottbar) m_GroovyObject).paint(g, dm);
        }
    }

    private void initCode() throws InstantiationException, IllegalAccessException, CompilationFailedException, IOException {
        GroovyClassLoader gcl = new GroovyClassLoader(PlottbaresGroovy.class.getClassLoader());
        Class<?> c = gcl.parseClass(m_Code);
        m_GroovyObject = (GroovyObject) c.newInstance();
    }

    private Object readResolve() throws ObjectStreamException {
        try {
            initCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public File getFile() {
        return m_Code;
    }

    @Override
    public String toString() {
        return "Groovy: " + getFile();
    }
}
