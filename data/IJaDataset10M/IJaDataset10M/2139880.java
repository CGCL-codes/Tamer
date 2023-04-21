package yamaloo.UrlCollector;

public class UrlCollectorConsole {

    public static void main(String[] args) {
        UrlCollector uc = new UrlCollector();
        uc.processSubRoot("http://gap.tmall.com/shop/view_shop.htm?prc=1&orderType=_hotkeep&search=y&viewType=grid&pageNum=1", "Gap", 37);
    }
}
