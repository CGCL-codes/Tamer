package net.mikaboshi.ant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Map.Entry;
import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.Task;

/**
 * 同じプロジェクト内のターゲットのdescription属性を出力するAntタスク。
 * 
 * @author Takuma Umezawa
 */
public class PrintDescriptionTask extends Task {

    private final M17NTaskLogger logger;

    public PrintDescriptionTask() {
        super();
        this.logger = new M17NTaskLogger(this, AntConstants.LOG_MESSAGE_BASE_NAME);
    }

    private List<String> targetNameList;

    /**
	 * ターゲットの出力順序を指定する。
	 * ここに指定されなかったターゲットは出力されない。
	 * @param order ターゲット名のリスト（カンマ、スペース、改行区切り）
	 */
    public void setOrder(String order) {
        if (order == null) {
            return;
        }
        this.targetNameList = new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(order, ", \r\n\t");
        while (st.hasMoreTokens()) {
            String targetName = st.nextToken();
            if (!getProject().getTargets().containsKey(targetName)) {
                this.logger.throwBuildException("error.invalid_target_name", targetName);
            }
            if (this.targetNameList.contains(targetName)) {
                this.logger.throwBuildException("error.duplicate_target_name", targetName);
            }
            this.targetNameList.add(targetName);
        }
    }

    /**
	 * 各ターゲットのdescription属性を出力する。
	 */
    @SuppressWarnings("unchecked")
    @Override
    public void execute() {
        Map<String, Target> targetMap = getProject().getTargets();
        if (this.targetNameList == null) {
            for (Entry<String, Target> entry : targetMap.entrySet()) {
                format(entry.getKey(), entry.getValue().getDescription());
            }
        } else {
            for (String name : this.targetNameList) {
                format(name, targetMap.get(name).getDescription());
            }
        }
    }

    protected void format(String name, String desc) {
        println(name + ":");
        if (StringUtils.isNotBlank(desc)) {
            println("    " + desc);
        }
    }

    protected void println(String str) {
        System.out.println(str);
    }
}
