package core;

import java.util.Date;

public class Transfer {

    private int index;

    private String project;

    private String file;

    private double progress;

    private int size;

    private Date elapsedTime;

    private double speed;

    private int status;

    public Transfer(int index, String project, String file, double progress, int size, Date elapsedTime, double speed, int status) {
        this.index = index;
        this.project = project;
        this.file = file;
        this.progress = progress;
        this.size = size;
        this.elapsedTime = elapsedTime;
        this.speed = speed;
        this.status = status;
    }
}
