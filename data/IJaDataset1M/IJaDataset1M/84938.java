package net.jtools.shovel;

import net.jtools.util.AntStringReplacer;
import net.jtools.util.DiagnosticContainer;
import net.jtools.util.ResultContainer;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Reference;
import org.jtools.meta.meta_inf.antlib.AntDef;
import org.jtools.meta.meta_inf.antlib.AntLib;
import org.jtools.meta.meta_inf.antlib.DefType;
import org.jtools.shovel.Shovel;
import org.jtools.shovel.spi.ComponentSPI;
import org.jtools.shovel.spi.SimpleSPISupport;
import org.jtools.util.SimpleNamedValue;

@AntLib({ @AntDef(type = DefType.TASK, value = "shovel"), @AntDef(type = DefType.TASK, value = "data") })
public class ShovelAntTask extends Task {

    public static class AntSupport extends SimpleSPISupport<AntSupport> {

        private final ProjectComponent projectComponent;

        private final AntStringReplacer stringReplacer;

        public AntSupport(ProjectComponent projectComponent) {
            this.projectComponent = projectComponent;
            this.stringReplacer = new AntStringReplacer(projectComponent);
            setStringReplacer(stringReplacer);
        }

        public Project getProject() {
            return projectComponent.getProject();
        }

        public SimpleNamedValue<String, String> createMacroAttribute() {
            return stringReplacer.createMacroAttribute();
        }
    }

    private final AntSupport support;

    private final Shovel shovel;

    public ShovelAntTask() {
        this.support = new AntSupport(this);
        this.shovel = new Shovel(this.support);
    }

    public void setDiagnosticRefid(Reference ref) {
        shovel.setDiagnosticListener(((DiagnosticContainer) ref.getReferencedObject()).getDiagnosticListener());
        shovel.setPrintDiagnostics(false);
    }

    public SimpleNamedValue<String, String> createMacroAttribute() {
        return support.createMacroAttribute();
    }

    public void setResultRefid(Reference ref) {
        shovel.setResultCollector(((ResultContainer) ref.getReferencedObject()).getLines());
        shovel.setPrintResults(false);
    }

    public void setPrintDiagnostics(boolean onOff) {
        shovel.setPrintDiagnostics(onOff);
    }

    public void setPrintResults(boolean onOff) {
        shovel.setPrintResults(onOff);
    }

    public void add(ComponentSPI componentSPI) {
        shovel.add(componentSPI);
    }

    public void execute() {
        shovel.execute();
    }

    public void setProgress(int n) {
        shovel.setProgress(n);
    }

    public void setSkip(long lines) {
        shovel.setSkip(lines);
    }

    public void setOptional(boolean onOff) {
        shovel.setOptional(onOff);
    }

    @Override
    public void setTaskName(String name) {
        super.setTaskName(name);
        shovel.setLogname(name);
    }
}
