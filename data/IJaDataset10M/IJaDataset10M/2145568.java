package jpaperwork.web;

/**
 * User: fleipold
* Date: Nov 8, 2009
* Time: 10:04:34 PM
*/
public class TextFragment extends Fragment {

    final String text;

    public String getText() {
        return text;
    }

    public TextFragment(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TextFragment)) return false;
        TextFragment that = (TextFragment) o;
        if (text != null ? !text.equals(that.text) : that.text != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return text != null ? text.hashCode() : 0;
    }
}
