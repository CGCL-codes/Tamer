package ctrlroot;

import java.lang.Class;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

@SuppressWarnings({ "unchecked" })
public class InProcessComponent extends Component implements IInProcessComponent {

    /**
     * The object we host
     */
    private Object m_client;

    public Object GetClient() {
        return m_client;
    }

    /**
     * The class of object we host
     * May appear redundant but designed to survive generics runtime time erasure
     */
    private Class m_clientClass;

    /**
     * Index of pipes to which we are connected
     */
    private String[] m_sPipeEndPoints;

    /**
     * Index of pools to which we are connected
     */
    private String[] m_sPoolEndPoints;

    /**
     * Index of pipe reader functions exposed by the object we host
     */
    private final HashMap<String, Method> m_pipeReaders = new HashMap<String, Method>();

    /**
     * Index of pool update notifier functions exposed bythe object we host
     */
    private final HashMap<String, Method> m_poolUpdaters = new HashMap<String, Method>();

    InProcessComponent() {
    }

    public void Init(String sId) {
        super.Init(sId);
    }

    public void Init(String sId, Class clientClass, String[] sPoolEndPoints, String[] sPipeEndPoints) {
        Init(sId);
        m_clientClass = clientClass;
        m_sPipeEndPoints = sPipeEndPoints;
        m_sPoolEndPoints = sPoolEndPoints;
    }

    public boolean Ping() {
        return true;
    }

    public void Start() {
        Thread m_Thread = new Thread(this, m_sId);
        m_Thread.start();
    }

    public boolean OnPipeRead(String sEndPoint, String[] tags, Object[] arglist) {
        String PipeReader = "OnPipeRead" + sEndPoint;
        if (m_pipeReaders.containsKey(PipeReader)) {
            Method method = m_pipeReaders.get(PipeReader);
            try {
                method.invoke(m_client, tags, arglist);
            } catch (IllegalAccessException e) {
                e.printStackTrace(System.out);
                return false;
            } catch (InvocationTargetException e) {
                e.printStackTrace(System.out);
                return false;
            }
        }
        return true;
    }

    public void OnPoolNotify(String sEndPoint, String sRecordID, String[] tags) {
        String OnPoolUpdater = "OnPoolNotify" + sEndPoint;
        if (m_poolUpdaters.containsKey(OnPoolUpdater)) {
            Method method = m_poolUpdaters.get(OnPoolUpdater);
            try {
                method.invoke(m_client, sRecordID, tags);
            } catch (IllegalAccessException e) {
                e.printStackTrace(System.out);
            } catch (InvocationTargetException e) {
                e.printStackTrace(System.out);
            }
        }
    }

    public void run() {
        try {
            m_client = m_clientClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        for (String m_sPipeEndPoint : m_sPipeEndPoints) {
            String OnPipeReaderFunction = "OnPipeRead" + m_sPipeEndPoint;
            Class[] partypes = new Class[2];
            partypes[0] = String[].class;
            partypes[1] = Object[].class;
            try {
                Method thisPipeReader;
                thisPipeReader = m_clientClass.getMethod(OnPipeReaderFunction, partypes);
                m_pipeReaders.put(OnPipeReaderFunction, thisPipeReader);
            } catch (NoSuchMethodException e) {
                System.out.println("Unable to find " + OnPipeReaderFunction + " in class " + m_clientClass.getName());
                System.out.println("Ok, if this class only expects to write to this pipe, otherwise reads will fail!");
            }
        }
        for (String s : m_sPoolEndPoints) {
            String OnPoolUpdater = "OnPoolNotify" + s;
            Class[] partypes = new Class[2];
            partypes[0] = String.class;
            partypes[1] = String[].class;
            try {
                Method thisPoolUpdateFunction;
                thisPoolUpdateFunction = m_clientClass.getMethod(OnPoolUpdater, partypes);
                m_poolUpdaters.put(OnPoolUpdater, thisPoolUpdateFunction);
            } catch (NoSuchMethodException e) {
                System.out.println("Unable to find " + OnPoolUpdater + " in class " + m_clientClass.getName());
                System.out.println("Ok, if this class is purely a publisher to this pool, otherwise reads will fail!");
            }
        }
        try {
            Method thisRun;
            thisRun = m_clientClass.getMethod("run");
            try {
                thisRun.invoke(m_client);
            } catch (InvocationTargetException e) {
                e.printStackTrace(System.out);
            } catch (IllegalAccessException e) {
                e.printStackTrace(System.out);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace(System.out);
        }
    }
}
