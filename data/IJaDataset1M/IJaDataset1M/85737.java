package org.lc.support.compass;

import org.apache.commons.lang.StringUtils;
import org.compass.spring.web.mvc.CompassSearchController;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 高级搜索的Controller.
 * 继承于{@link CompassSearchController}
 * 如果还需要增加更多的搜索条件,继承本类,并重载setUpCommand及referenceData类
 *
 */
public class AdvancedSearchController extends CompassSearchController {

    public AdvancedSearchController() {
        setCommandClass(AdvancedSearchCommand.class);
    }

    /**
     * 高级搜索页处理页,处理搜索条件页及搜索结果页的显示
     */
    protected ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        AdvancedSearchCommand asCommand = (AdvancedSearchCommand) command;
        asCommand = setUpCommand(asCommand);
        return super.handle(request, response, asCommand, errors);
    }

    /**
     * 将高级搜索条件加工成符合语法的quary字符串
     */
    protected AdvancedSearchCommand setUpCommand(AdvancedSearchCommand command) {
        StringBuffer sb = new StringBuffer();
        if (StringUtils.isNotBlank(command.getFull())) {
            Object[] texts = splitQuery(command.getFull());
            for (int i = 0; i < texts.length; i++) {
                if (StringUtils.isEmpty((String) texts[i])) continue;
                sb.append(texts[i]);
                if (i != texts.length - 1) sb.append(" AND ");
            }
        }
        if (StringUtils.isNotBlank(command.getAnyone())) {
            sb.append(" ");
            sb.append(command.getAnyone());
            sb.append(" ");
        }
        if (StringUtils.isNotBlank(command.getExclude())) {
            Object[] texts = splitQuery(command.getExclude());
            for (Object text : texts) {
                if (StringUtils.isEmpty((String) text)) continue;
                sb.append(" -");
                sb.append(text);
            }
        }
        onSetUpCommand(command, sb);
        if (StringUtils.isNotEmpty(sb.toString())) {
            command.setQuery(sb.toString());
        }
        if (command.getPageSize() != null) {
            this.setPageSize(command.getPageSize());
        }
        referenceData(command);
        return command;
    }

    /**
     * 分割查询字符串成数组，保留""字符
     * eg. "aa bb" cc => String[]{"aa bb",cc}
     * aa bb cc  => String[]{aa, bb,cc}
     */
    @SuppressWarnings("unchecked")
    protected Object[] splitQuery(String query) {
        List list = new ArrayList();
        Pattern pattern = Pattern.compile("\\s*(\\u0022\\s*\\S+.*\\S+\\s*\\u0022)|(\\S+)\\s*");
        Matcher matcher = pattern.matcher(query);
        while (matcher.find()) {
            String key = matcher.group(1) != null ? matcher.group(1) : matcher.group(2);
            list.add(key);
        }
        return list.toArray();
    }

    /**
     * 进一步加工条件的回调函数,在子类重载
     */
    protected void onSetUpCommand(AdvancedSearchCommand command, StringBuffer sb) {
    }

    /**
     * 将"类别列表"等辅助显示的数据放入command中的回调函数,在子类重载.
     */
    protected void referenceData(AdvancedSearchCommand command) {
    }
}
