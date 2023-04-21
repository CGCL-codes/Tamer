package jasci.ui.text;

public class WrappedPlainView extends FlowView {

    boolean wordWrap;

    class WrappedTextView extends PlainTextView {

        Position start, end;

        WrappedTextView(Element e) {
            super(e);
            start = null;
            end = null;
        }

        public int getStartOffset() {
            if (start != null) return start.offset(); else return super.getStartOffset();
        }

        public int getEndOffset() {
            if (end != null) return end.offset(); else return super.getEndOffset();
        }

        public View[] breakView(int axis, int len) {
            if (axis != View.X_AXIS) return super.breakView(axis, len);
            Document doc = element.getDocument();
            int startOffset = getStartOffset();
            int endOffset = getEndOffset();
            int breakPosition;
            if (len >= endOffset - startOffset) return super.breakView(axis, len);
            if (wordWrap) {
                String text = this.content();
                int i = len;
                while (i > 0 && text.charAt(i) != ' ') i--;
                if (i > 0) {
                    while (i < text.length() && text.charAt(i) == ' ') i++;
                    breakPosition = i;
                } else return super.breakView(axis, len);
            } else breakPosition = len;
            WrappedTextView firstView = new WrappedTextView(element);
            firstView.start = doc.createPosition(startOffset);
            firstView.end = doc.createPosition(startOffset + breakPosition - 1);
            WrappedTextView secondView = new WrappedTextView(element);
            secondView.start = doc.createPosition(startOffset + breakPosition);
            secondView.end = doc.createPosition(endOffset);
            View views[] = new View[2];
            views[0] = firstView;
            views[1] = secondView;
            return views;
        }
    }

    public void setupChildren() {
        int i;
        for (i = 0; i < element.getElementCount(); i++) {
            Element e = element.getElement(i);
            append(new WrappedTextView(e));
        }
    }

    public WrappedPlainView(Element element, boolean wordWrap) {
        super(element);
        this.wordWrap = wordWrap;
    }
}
