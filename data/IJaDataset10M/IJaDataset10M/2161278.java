package nl.huub.van.amelsvoort.sound;

public class sfx_t {

    public String name;

    public int registration_sequence;

    public sfxcache_t cache;

    public String truename;

    public int bufferId = -1;

    public boolean isCached = false;

    public void clear() {
        name = truename = null;
        cache = null;
        registration_sequence = 0;
        bufferId = -1;
        isCached = false;
    }
}
