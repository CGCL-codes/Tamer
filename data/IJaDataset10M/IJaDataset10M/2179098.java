package org.fao.waicent.util;

public interface interfaceMessageProcess {

    public void Message(String sMessage);

    public void UpdateCounter(int counter);

    public void SetCounter(int counter);

    public void StartCounter();

    public void EndCounter();

    public void EndProcess();
}
