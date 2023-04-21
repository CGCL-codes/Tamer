package de.knowwe.kdom.dashtree;

import java.util.ArrayList;
import java.util.List;
import de.knowwe.core.kdom.AbstractType;
import de.knowwe.core.kdom.Type;
import de.knowwe.core.kdom.parsing.Section;
import de.knowwe.core.kdom.rendering.DefaultTextRenderer;
import de.knowwe.core.kdom.rendering.Renderer;
import de.knowwe.core.kdom.sectionFinder.SectionFinder;
import de.knowwe.core.kdom.sectionFinder.SectionFinderResult;
import de.knowwe.core.user.UserContext;

public class DashesPrefix extends AbstractType {

    public DashesPrefix() {
        this.sectionFinder = new DashesPrefixFinder();
        this.setRenderer(new Renderer() {

            @Override
            public void render(Section<?> sec, UserContext user, StringBuilder string) {
                if (sec.getText().trim().startsWith("-")) {
                    string.append('~');
                }
                DefaultTextRenderer.getInstance().render(sec, user, string);
            }
        });
    }

    class DashesPrefixFinder implements SectionFinder {

        @Override
        public List<SectionFinderResult> lookForSections(String text, Section<?> father, Type type) {
            int leadingSpaces = text.indexOf(text.trim());
            int index = leadingSpaces;
            while (text.charAt(index) == '-') {
                index++;
            }
            ArrayList<SectionFinderResult> result = new ArrayList<SectionFinderResult>();
            result.add(new SectionFinderResult(0, index));
            return result;
        }
    }
}
