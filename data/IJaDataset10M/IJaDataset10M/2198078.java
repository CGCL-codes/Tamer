package fitlibrary.specify;

import fitlibrary.DoFixture;
import fitlibrary.tagged.TaggedString;

public class DoWithTags extends DoFixture {

    public TaggedString taggedText() {
        return new TaggedString("<b>bold</b>");
    }

    public TaggedString tagText(TaggedString s) {
        return s;
    }
}
