package de.iritgo.aktera.base.tools.jvmmemory;

import java.awt.image.BufferedImage;

public interface AkteraJvmMemoryManager {

    public static String ID = "de.iritgo.aktera.AkteraJvmMemoryManager";

    public static int DATA_CAPTURE_INTERVAL = 60 * 5;

    public void generateGraph(String name, BufferedImage bufferedImage);

    public void setStartTime(int startTime);
}
