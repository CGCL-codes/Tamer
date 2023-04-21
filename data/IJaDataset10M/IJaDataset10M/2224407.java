package annone.server.web;

public class FilterBind extends PageBind {

    @Override
    public void perform(WebContext context) {
        checkChannelOrRedirect(context);
    }
}
