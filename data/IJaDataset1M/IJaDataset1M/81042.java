package br.biofoco.p2p.scheduling;

public interface Task {

    public long getID();

    public TaskStatus getStatus();

    public void setStatus(TaskStatus newStatus);

    public void setReplicaCount(int i);

    public int getReplicaCount();

    public void increaseReplicaCount();
}
