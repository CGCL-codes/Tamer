package org.modulefusion.example.ipojo.simple;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Provides;

/**
 * Example class that gets registered as a OSGi service
 * 
 * @author clement@apache.org (clement escoffier)
 */
@Component
@Provides
public class ExportedAsAService implements Runnable {

    public void run() {
        System.out.println("Running...");
    }
}
