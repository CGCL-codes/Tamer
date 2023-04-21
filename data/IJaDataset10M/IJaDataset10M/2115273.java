package org.stumeikle.Yggdrasil;

import java.net.*;
import java.io.*;
import java.util.*;

/** BodyFactory base class. 
 *  new 20061112. BodyFactories are used to create new body instances in accordance with the
 *  body type string received via the nervous system. Users can overload this base class. 
 *
 */
public interface BodyFactory {

    public Body createNewBody(String t);
}
