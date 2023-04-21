package net.saim.game.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Task implements IsSerializable {

    /**
   * HashMap that will always contain strings for both keys and values
   * @gwt.typeArgs <java.lang.String>
   */
    private String[] task;

    public Task() {
        super();
        task = new String[5];
    }

    /**
   * @return the subject
   */
    public String getSubject() {
        return task[0];
    }

    /**
   * @param subject the subject to set
   */
    public void setSubject(String subject) {
        this.task[0] = subject;
    }

    /**
   * @return the object
   */
    public String getObject() {
        return this.task[1];
    }

    /**
   * @param object the object to set
   */
    public void setObject(String object) {
        this.task[1] = object;
    }

    /**
   * @return the file
   */
    public String getFile() {
        return this.task[2];
    }

    /**
   * @param file the file to set
   */
    public void setFile(String file) {
        this.task[2] = file;
    }

    public void setTask(String[] task) {
        this.task = task;
    }

    public String getDate() {
        return this.task[3];
    }
}
