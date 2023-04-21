package rt;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;
import mms.EnvironmentAgent;
import mms.Event;
import mms.EventServer;

public class AudioEventServer extends EventServer {

    private ConcurrentHashMap<String, float[]> sample_queues = new ConcurrentHashMap<String, float[]>();

    private static final int QUEUE_FRAMES = 50;

    private int queue_end;

    private float sound_speed = 0.343f;

    private float referenceDistance = 1.0f;

    private float attenuation_factor = 0.1f;

    private int sample_rate = 44100;

    private int chunk_size = 11025;

    private RT_World world;

    public AudioEventServer(EnvironmentAgent envAgent) {
        super(envAgent, "SOUND", 250, 100, 200, 1000, null);
    }

    @Override
    public void init(Hashtable<String, String> parameters) {
        world = (RT_World) envAgent.getWorld();
        queue_end = 0;
    }

    public Hashtable<String, String> actuatorRegistered(String agentName, String eventHandlerName, Hashtable<String, String> userParam) {
        String key = agentName + ":" + eventHandlerName;
        float[] value = new float[chunk_size * QUEUE_FRAMES];
        sample_queues.put(key, value);
        System.out.println("[AudioEventServer] New queue created " + key + " of size " + (chunk_size * QUEUE_FRAMES));
        Hashtable<String, String> userParameters = new Hashtable<String, String>(1);
        userParameters.put("chunk_size", String.valueOf(chunk_size));
        userParameters.put("sample_rate", String.valueOf(sample_rate));
        return userParameters;
    }

    public void actuatorDeregistered(String agentName, String eventHandlerName) {
        String key = agentName + ":" + eventHandlerName;
        sample_queues.remove(key);
    }

    @Override
    public void process() {
        queue_end = queue_end + chunk_size;
        if (queue_end >= chunk_size * QUEUE_FRAMES) {
            queue_end = queue_end - (chunk_size * QUEUE_FRAMES);
        }
        System.out.println("queue_end = " + queue_end);
        for (Enumeration<String> e = sensors.keys(); e.hasMoreElements(); ) {
            String e_key = e.nextElement();
            SoundEvent evt = new SoundEvent(chunk_size);
            String[] sensor = e_key.split(":");
            evt.destAgentName = sensor[0];
            evt.destAgentCompName = sensor[1];
            for (Enumeration<String> f = sample_queues.keys(); f.hasMoreElements(); ) {
                String f_key = f.nextElement();
                System.out.println("ENTREI " + f_key);
                float[] queue = sample_queues.get(f_key);
                String src_pos = world.agentsPosition.get(f_key.split(":")[0]);
                float src_pos_x = Float.valueOf(src_pos.split(":")[0]);
                float src_pos_y = Float.valueOf(src_pos.split(":")[1]);
                String dest_pos = world.agentsPosition.get(sensor[0]);
                float dest_pos_x = Float.valueOf(dest_pos.split(":")[0]);
                float dest_pos_y = Float.valueOf(dest_pos.split(":")[1]);
                float distance = (float) Math.sqrt(Math.pow(Math.abs(src_pos_x - dest_pos_x), 2) + Math.pow(Math.abs(src_pos_y - dest_pos_y), 2));
                System.out.println("distance = " + distance);
                float t_delay = distance / sound_speed;
                System.out.println("t_delay = " + t_delay);
                int sample_delay = Math.round((sample_rate * t_delay) / 1000);
                System.out.println("sample_delay = " + sample_delay);
                int start_sample = queue_end - sample_delay - chunk_size;
                if (start_sample < 0) {
                    start_sample = queue.length + start_sample;
                }
                System.out.println("start_sample = " + start_sample);
                float gain = 1.0f;
                if (attenuation_factor > 0.0f && distance > referenceDistance) {
                    gain = referenceDistance / (attenuation_factor * (referenceDistance + (distance - referenceDistance)));
                    if (gain > 1.0f) {
                        gain = 1.0f;
                    }
                }
                System.out.println("gain = " + gain);
                int ptr = start_sample;
                for (int i = 0; i < chunk_size; i++, ptr++) {
                    if (ptr == queue.length) {
                        ptr = 0;
                    }
                    evt.chunk[i] = evt.chunk[i] + (queue[ptr] * gain);
                }
            }
            outputEvents.add(evt);
        }
    }

    @Override
    public Event processAction() {
        return null;
    }

    public void processSense(Event evt) {
        SoundEvent audio_evt = (SoundEvent) evt;
        String key = evt.oriAgentName + ":" + evt.oriAgentCompName;
        float[] queue = sample_queues.get(key);
        if (queue == null) {
            System.out.println("[AudioEventServer] Fila de " + key + " n�o existe!");
            return;
        }
        int fits = 0;
        if (chunk_size <= (queue.length - queue_end)) {
            fits = chunk_size;
        } else {
            fits = queue.length - queue_end;
        }
        int queue_ptr = queue_end;
        int chunk_ptr = 0;
        for (int i = 0; i < fits; i++, queue_ptr++, chunk_ptr++) {
            queue[queue_ptr] = audio_evt.chunk[chunk_ptr];
        }
        if (queue_ptr == queue.length) {
            queue_ptr = 0;
        }
        for (int j = 0; j < chunk_size - fits; j++, queue_ptr++, chunk_ptr++) {
            queue[queue_ptr] = audio_evt.chunk[chunk_ptr];
        }
    }
}
