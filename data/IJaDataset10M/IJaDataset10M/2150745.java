package au.edu.diasb.annotation.dannotate;

public class NoDannotateAccessPolicy implements DannoateAccessPolicy {

    @Override
    public boolean canChangeNames() {
        return true;
    }

    @Override
    public void checkCreateAnnotation() {
    }

    @Override
    public void checkCreateReply() {
    }

    @Override
    public void checkDelete() {
    }

    @Override
    public void checkEditAnnotation() {
    }

    @Override
    public void checkEditReply() {
    }
}
