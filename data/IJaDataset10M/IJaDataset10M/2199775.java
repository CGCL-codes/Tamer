package protoj.system;

import protoj.build.Internal;
import protoj.build.ProtoProject;

@Internal
public class ProjectVm {

    public static void main(String[] args) {
        new ProtoProject(args, ProjectVm.class).execute();
    }
}
