package com.cosylab.vdct.model.db.db;

/**
 * Insert the type's description here.
 * Creation date: (29.1.2001 17:50:31)
 * @author Matej Sekoranja
 */
public interface OutLink extends Linkable {

    public static final int NORMAL_MODE = 0;

    public static final int INVISIBLE_MODE = 1;

    public static final int EXTERNAL_OUTPUT_MODE = 2;

    public static final int EXTERNAL_INPUT_MODE = 3;

    public static final int CONSTANT_PORT_MODE = 4;

    public static final int INPUT_PORT_MODE = 5;

    public static final int OUTPUT_PORT_MODE = 6;

    /**
 * 
 * @return com.cosylab.vdct.graphics.objects.InLink
 */
    InLink getInput();

    /**
 * Insert the method's description here.
 * Creation date: (29.1.2001 17:53:09)
 * @return int
 */
    int getOutX();

    /**
 * Insert the method's description here.
 * Creation date: (29.1.2001 17:53:18)
 * @return int
 */
    int getOutY();

    /**
 * Insert the method's description here.
 * Creation date: (30.1.2001 14:46:40)
 * @return int
 */
    int getQueueCount();

    /**
 * Insert the method's description here.
 * Creation date: (29.1.2001 17:53:59)
 */
    void setInput(InLink input);

    /**
 * Insert the method's description here.
 * Creation date: (29.1.2001 17:53:09)
 * @return int
 */
    int getMode();

    void validateLink();

    int getLeftX();

    int getRightX();

    boolean isRight();
}
