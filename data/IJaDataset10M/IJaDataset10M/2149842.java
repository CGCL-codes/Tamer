package projectbuilder.options;

import projectviewer.config.OptionsService;
import projectviewer.vpt.VPTProject;
import org.gjt.sp.jedit.OptionPane;
import org.gjt.sp.jedit.OptionGroup;
import org.gjt.sp.util.Log;

public class BuildRunOptionsService implements OptionsService {

    public OptionPane getOptionPane(VPTProject proj) {
        return new BuildRunOptionsPane(proj);
    }

    public OptionGroup getOptionGroup(VPTProject proj) {
        return null;
    }
}
