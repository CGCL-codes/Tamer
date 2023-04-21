package org.vrspace.vrmlclient;

import org.vrspace.util.*;
import java.lang.reflect.*;
import java.net.URL;
import java.util.*;
import vrml.external.*;
import vrml.external.field.*;
import vrml.external.exception.*;

/**
Only one reason for this class (so far): if client isn't publish as a jar file
but as collection of class files, client tries to load that class each time it
encounters VrmlFile object. Larger the world, more NoClassDefFoundExceptions
we get. The trouble happens over http: VrmlFile -> gimme the class -> 404 -> Exception,
for each and every vrml file! This proces takes hundereds of ms over the internet - per file!
*/
public class VrmlFile extends NodeManager {
}
