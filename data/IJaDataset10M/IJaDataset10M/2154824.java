package cornell.herbivore.system;

import java.lang.*;
import java.io.*;
import cornell.herbivore.util.Log;
import cornell.herbivore.util.*;

public class HerbivoreUnicastDatagramServerSocket extends HerbivoreDatagramServerSocket {

    protected HerbivoreUnicastDatagramServerSocket(HerbivoreClique asrcClique, short asrvport) {
        super(asrcClique, asrvport);
    }
}
