package jp.seraph.same.model;

import java.io.IOException;
import jp.seraph.jsmf.motion.EditableMotion;
import jp.seraph.jsmf.motion.Motion;

public interface MotionCollectionModel {

    public int getMotionCount();

    public String getMotionName(int aIndex);

    public MotionModel getMotion(String aMotionName);

    public MotionModel getMotion(int aIndex);

    public void setMotion(String aMotionName, EditableMotion aMotion);

    public void setMotion(String aMotionName, MotionModel aMotion);

    public void removeMotion(String aMotionName);

    public void selectMotion(int aIndex);

    public void selectMotion(String aMotionName);

    public int getSelectedIndex();

    public String getSelectedMotionName();

    public MotionModel getSelectedMotion();

    /**
     * 可能であれば、何らかの手段でモーション列を永続化する．
     */
    public void save() throws IOException;
}
