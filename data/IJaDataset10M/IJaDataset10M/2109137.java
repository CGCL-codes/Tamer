package ctags.sidekick.renderers;

import ctags.sidekick.AbstractObjectProcessor;
import ctags.sidekick.IObjectProcessor;
import ctags.sidekick.Tag;

public class NameAndSignatureTextProvider extends AbstractObjectProcessor implements ITextProvider {

    static final String NAME = "Name and signature";

    static final String DESCRIPTION = "Tag name and signature (for functions)";

    public NameAndSignatureTextProvider() {
        super(NAME, DESCRIPTION);
    }

    public String getString(Tag tag) {
        String name = tag.getName();
        String signature = tag.getField("signature");
        if (signature != null && signature.length() > 0) return name + signature;
        return name;
    }

    public IObjectProcessor getClone() {
        return new NameAndSignatureTextProvider();
    }
}
