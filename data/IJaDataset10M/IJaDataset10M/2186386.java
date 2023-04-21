package sourcefiles;

public abstract class Buch {

    protected String sAuthor;

    protected String sTitle;

    protected int iPages;

    public int getPages() {
        return iPages;
    }

    public String getTitle() {
        return sTitle;
    }

    public void setPages(int _page) {
        iPages = _page;
    }

    public void setTitle(String _title) {
        sTitle = _title;
    }

    public void setAuthor(String _auth) {
        sAuthor = _auth;
    }

    public String getAuthor() {
        return sAuthor;
    }

    public abstract void showAll();
}
