package net.wimpi.text;

import java.util.*;

/**
 * Class that implements a kernel for handling the main
 * tasks of the processing framework.
 * It represents a singleton, and can be used to retrieve or
 * add named <code>Processor</code> or <code>ProcessingPipe</code>
 * instances, the same as named <code>ResourcePool</code> instances.
 *
 * There are also utility methods for bootstrapping from a
 * <code>Properties<code> instance, and returning the current
 * state as <code>Properties</code> instance.
 *
 * @author Dieter Wimberger
 * @version 0.2 19/11/2002
 *
 * @see net.wimpi.text.Processor
 * @see net.wimpi.text.ProcessingPipe
 *
 */
public class ProcessingKernel {

    private HashMap m_Pipes;

    private HashMap m_Processors;

    private HashMap m_ResourcePools;

    private static ProcessingKernel c_Self = null;

    /**
	 * Constructs a new kernel instance.
	 */
    private ProcessingKernel() {
        c_Self = this;
    }

    /**
	 * Lists the available processing pipes with their 
	 * respective name.
	 * 
	 * @return a <code>String[]</code> containing a list of
	 *         processing pipe names.
	 */
    public String[] listProcessingPipes() {
        return getKeyList(m_Pipes);
    }

    /**
	 * Returns a reference to a <code>ProcessingPipe</code> 
	 * instance if available for the given name.
	 * 
	 * @param name the name of the pipe to be retrieved.
	 *
	 * @return the reference to the <code>ProcessingPipe</code> instance.
	 */
    public ProcessingPipe getProcessingPipe(String name) {
        Object o = m_Pipes.get(name);
        return ((o != null) ? ((ProcessingPipe) o) : null);
    }

    /**
	 * Adds a processing pipe to this kernel.
	 *
	 * @param pipe the <code>ProcessingPipe</code> to be added.
	 */
    public void addProcessingPipe(ProcessingPipe pipe) {
        m_Pipes.put(pipe.getName(), pipe);
    }

    /**
	 * Removes the given <code>ProcessingPipe</code> from the kernel.
	 * 
	 * @param pipe the <code>ProcessingPipe</code> instance to be removed.
	 */
    public void removeProcessingPipe(ProcessingPipe pipe) {
        m_Pipes.remove(pipe.getName());
    }

    /**
	 * Lists the available resource pools with their 
	 * respective name.
	 * 
	 * @return a <code>String[]</code> containing a list of
	 *         resource pool names.
	 */
    public String[] listResourcePools() {
        return getKeyList(m_ResourcePools);
    }

    /**
	 * Returns a reference to a <code>ResourcePool</code> 
	 * instance if available for the given name.
	 * 
	 * @param name the name of the pool to be retrieved.
	 *
	 * @return the reference to the <code>ResourcePool</code> instance.
	 */
    public ResourcePool getResourcePool(String name) {
        Object o = m_ResourcePools.get(name);
        return ((o != null) ? ((ResourcePool) o) : null);
    }

    /**
	 * Adds a resource pool with the given name to this kernel.
	 *
	 * @param name the name of the pool.
	 * @param pool the <code>ResourcePool</code> to be added.
	 */
    public void addResourcePool(String name, ResourcePool pool) {
        m_ResourcePools.put(name, pool);
    }

    /**
	 * Removes the <code>ResourcePool</code> with the given name
	 * from the kernel.
	 * 
	 * @param name the name of the <code>ResourcePool</code> to be removed.
	 */
    public void removeResourcePool(String name) {
        m_ResourcePools.remove(name);
    }

    /**
	 * Lists the available processors with their 
	 * respective name.
	 * 
	 * @return a <code>String[]</code> containing a list of
	 *         processor names.
	 */
    public String[] listProcessors() {
        return getKeyList(m_Processors);
    }

    /**
	 * Returns a reference to a <code>Processor</code> 
	 * instance if available for the given name.
	 * 
	 * @param name the name of the processor to be retrieved.
	 *
	 * @return the reference to the <code>Processor</code> instance.
	 */
    public Processor getProcessor(String name) {
        Object o = m_Processors.get(name);
        return ((o != null) ? ((Processor) o) : null);
    }

    /**
	 * Adds a processor to this kernel.
	 *
	 * @param processor the <code>Processor</code> to be added.
	 */
    public void addProcessor(Processor processor) {
        m_Processors.put(processor.getName(), processor);
    }

    /**
	 * Removes the given <code>Processor</code> from the kernel.
	 * 
	 * @param processor the <code>Processor</code> instance to be removed.
	 */
    public void removeProcessor(Processor processor) {
        m_Processors.remove(processor.getName());
    }

    /**
	 * Utility method that loads resource pools from the
	 * given <code>Properties</code> instance.
	 *
	 */
    private void loadResourcePools(Properties props) throws Exception {
        String res = props.getProperty("resourcepools");
        if (res == null || res.equals("")) {
            m_ResourcePools = new HashMap(10);
            return;
        }
        String[] pools = ProcessingKernel.split(res, ",");
        m_ResourcePools = new HashMap((int) (pools.length * 1.4));
        for (int i = 0; i < pools.length; i++) {
            ProcessingResource resource = (ProcessingResource) Class.forName(props.getProperty("respool." + pools[i] + ".resource.class")).newInstance();
            int poolsize = 0;
            poolsize = Integer.parseInt(props.getProperty("respool." + pools[i] + ".size"));
            ResourcePool newpool = ResourcePool.createResourcePool(poolsize, resource);
            m_ResourcePools.put(pools[i], newpool);
        }
    }

    /**
	 * Utility method that loads processors from the 
	 * given <code>Properties</code> instance.
	 */
    private void loadProcessors(Properties props) throws Exception {
        String procs = props.getProperty("processors");
        if (procs == null || procs.equals("")) {
            throw new Exception("Properties don't specify any processors.");
        } else {
            String[] processors = ProcessingKernel.split(procs, ",");
            m_Processors = new HashMap((int) (processors.length * 1.4));
            for (int i = 0; i < processors.length; i++) {
                Processor newproc = (Processor) Class.forName(props.getProperty("processor." + processors[i] + ".class")).newInstance();
                String locales = props.getProperty("processor." + processors[i] + ".description.locales");
                if (locales == null || locales.equals("")) {
                    throw new Exception("Properties don't specify locales for processor " + processors[i] + ".");
                } else {
                    String[] locale = ProcessingKernel.split(locales, ",");
                    for (int j = 0; j < locale.length; j++) {
                        Locale l = new Locale(locale[j], "");
                        String description = props.getProperty("processor." + processors[i] + ".description." + locale[j]);
                        if (description == null || description.equals("")) {
                            throw new Exception("Properties don't specify the description for a listed processor locale.");
                        } else {
                            newproc.setDescription(description, l);
                        }
                    }
                }
                m_Processors.put(processors[i], newproc);
            }
        }
    }

    /**
	 * Utility method that loads processing pipes from the 
	 * given <code>Properties</code> instance.
	 */
    private void loadProcessingPipes(Properties props) throws Exception {
        String pipes = props.getProperty("pipes");
        if (pipes == null || pipes.equals("")) {
            m_Pipes = new HashMap(10);
            return;
        } else {
            String[] allpipes = ProcessingKernel.split(pipes, ",");
            m_Pipes = new HashMap((int) (allpipes.length * 1.4));
            for (int i = 0; i < allpipes.length; i++) {
                ProcessingPipe pipe = new ProcessingPipeImpl(allpipes[i]);
                String[] procnames = ProcessingKernel.split(props.getProperty("pipe." + allpipes[i] + ".sequence"), ",");
                for (int j = 0; j < procnames.length; j++) {
                    Processor proc = getProcessor(procnames[j]);
                    if (proc == null || proc.equals("")) {
                        continue;
                    } else {
                        pipe.addProcessor(proc);
                    }
                }
                String locales = props.getProperty("pipe." + allpipes[i] + ".description.locales");
                if (locales == null || locales.equals("")) {
                    throw new Exception("Properties don't specify locales for pipe " + allpipes[i] + ".");
                } else {
                    String[] locale = ProcessingKernel.split(locales, ",");
                    for (int j = 0; j < locale.length; j++) {
                        Locale l = new Locale(locale[j], "");
                        String description = props.getProperty("pipe." + allpipes[i] + ".description." + locale[j]);
                        if (description == null || description.equals("")) {
                            throw new Exception("Properties don't specify the description for a listed pipe locale." + "pipe." + allpipes[i] + ".description." + locale[j]);
                        } else {
                            pipe.setDescription(description, l);
                        }
                    }
                }
                m_Pipes.put(pipe.getName(), pipe);
            }
        }
    }

    /**
	 * Returns a <code>Properties instance that
	 * reflects the actual state of the Kernel.
	 * 
	 * @return the reference to the created <code>Properties</code> instance.
	 */
    public Properties dumpKernel() {
        Properties props = new Properties();
        String[] list = listResourcePools();
        props.put("resourcepools", joinList(list));
        for (int i = 0; i < list.length; i++) {
            ResourcePool pool = getResourcePool(list[i]);
            ProcessingResource res = pool.leaseResource();
            Class rescl = res.getClass();
            props.put("respool." + list[i] + "resource.class", rescl.getName());
            props.put("respool" + list[i] + ".size", "" + pool.getCeiling());
            pool.releaseResource(res);
        }
        list = listProcessors();
        props.put("processors", joinList(list));
        for (int i = 0; i < list.length; i++) {
            Processor proc = getProcessor(list[i]);
            Class rescl = proc.getClass();
            props.put("processor." + list[i] + ".class", rescl.getName());
            Locale[] locales = proc.listAvailableLocales();
            String[] languages = new String[locales.length];
            for (int j = 0; j < locales.length; j++) {
                languages[j] = locales[j].getLanguage();
                props.put("processor." + list[i] + ".description." + languages[j], proc.getDescription(locales[j]));
            }
            props.put("processor." + list[i] + ".description.locales", joinList(languages));
        }
        list = listProcessingPipes();
        props.put("pipes", joinList(list));
        for (int i = 0; i < list.length; i++) {
            ProcessingPipe pipe = getProcessingPipe(list[i]);
            props.put("pipe." + pipe.getName() + ".sequence", pipe.toString());
            Locale[] locales = pipe.listAvailableLocales();
            String[] languages = new String[locales.length];
            for (int j = 0; j < locales.length; j++) {
                languages[j] = locales[j].getLanguage();
                props.put("pipe." + list[i] + ".description." + languages[j], pipe.getDescription(locales[j]));
            }
            props.put("pipe." + list[i] + ".description.locales", joinList(languages));
        }
        return props;
    }

    /**
	 * Reloads the Kernel from a <code>Properties</code> instance.
	 *
	 * @param props a reference to a <code>Properties</code> instance
	 *        describing a kernel state.
	 */
    public void reloadKernel(Properties props) throws Exception {
        this.loadResourcePools(props);
        this.loadProcessors(props);
        this.loadProcessingPipes(props);
    }

    /**
	 * Returns the singleton reference of the unique Kernel
	 * instance.
	 * 
	 * @return the singleton instance reference.
	 */
    public static final ProcessingKernel getReference() {
        return c_Self;
    }

    /**
	 * Creates a Kernel from a given <code>Properties</code>
	 * instance.
	 * Note that this can only be use once without running into troubles.
	 */
    public static final ProcessingKernel createProcessingKernel(Properties props) throws Exception {
        if (c_Self != null) {
            throw new Exception("Singleton cannot be instantiated more then once.");
        }
        ProcessingKernel pk = new ProcessingKernel();
        try {
            pk.loadResourcePools(props);
            pk.loadProcessors(props);
            pk.loadProcessingPipes(props);
        } catch (Exception ex) {
            c_Self = null;
            throw ex;
        }
        return pk;
    }

    /**
     * Static method that retrieves the keys of a HashMap as
     * Strings.
     * 
     * @param tab HashMap with elements.
     *
     * @return String[] holding the HashMap's keys Strings.
     */
    private static String[] getKeyList(HashMap tab) {
        String[] strarr = new String[tab.size()];
        Set keys = tab.keySet();
        return (String[]) keys.toArray(strarr);
    }

    /**
	 * Method that splits a string with delimited fields
	 * into an array of field strings.
	 * 
	 * @param str String with delimited fields.
	 * @param delim String that represents the delimiter.
	 *
	 * @return String[] holding all fields.
	 */
    private static String[] split(String str, String delim) {
        StringTokenizer strtok = new StringTokenizer(str, delim);
        String[] result = new String[strtok.countTokens()];
        for (int i = 0; i < result.length; i++) {
            result[i] = strtok.nextToken();
        }
        return result;
    }

    private static String joinList(String[] items) {
        StringBuffer sbuf = new StringBuffer();
        for (int i = 0; i < items.length; i++) {
            sbuf.append(items[i]);
            if (i != items.length - 1) {
                sbuf.append(",");
            }
        }
        return sbuf.toString();
    }
}
