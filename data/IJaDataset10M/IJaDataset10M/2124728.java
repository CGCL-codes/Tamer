package com.bluebrim.layout.impl.client;

import java.util.*;
import javax.swing.*;
import com.bluebrim.layout.impl.client.editor.*;

/**
 * Protocol of custom layout editor operations.
 * 
 * @author: Dennis Malmstr�m
 */
public interface CoPageItemOperationIF {

    String getName();

    KeyStroke getShortcut();

    boolean isValidOperand(int operandCount);

    boolean isValidOperand(List operands);

    boolean canBeUndone();

    boolean doit(CoLayoutEditor editor, List operands);

    boolean redoit(CoLayoutEditor editor, List operands);

    boolean undoit(CoLayoutEditor editor, List operands);
}
