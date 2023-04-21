package libsvm;

import java.io.*;
import java.util.*;

abstract class QMatrix {

    abstract float[] get_Q(int column, int len);

    abstract float[] get_QD();

    abstract void swap_index(int i, int j);
}

;
