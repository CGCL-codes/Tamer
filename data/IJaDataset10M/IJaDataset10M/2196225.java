package archives.view.searchpanel.auto;

import java.util.ArrayList;

/**
 * 
 * @Description
 * @Author zhangzuoqiang
 * @Date 2011-12-30
 */
public interface CompletionFilter {

    ArrayList<?> filter(String text);
}
