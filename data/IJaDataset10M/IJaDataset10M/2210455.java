package mhk.form;

import mhk.dto.RemarkBean;
import org.seasar.struts.annotation.Required;

public class Thread7Form {

    public RemarkBean[] remarkItems;

    public String name;

    public int threadId;

    public int id;

    public int hoge;

    public String honbun;

    public int userId;

    public int iconId;

    public String color;

    @Required
    public String remark;
}
