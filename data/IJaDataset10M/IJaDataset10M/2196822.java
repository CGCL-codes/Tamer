package automap;

import i.DoomStatusAware;
import v.IVideoScaleAware;
import doom.event_t;

public interface IAutoMap extends IVideoScaleAware, DoomStatusAware {

    public final int AM_MSGHEADER = (('a' << 24) + ('m' << 16));

    public final int AM_MSGENTERED = (AM_MSGHEADER | ('e' << 8));

    public final int AM_MSGEXITED = (AM_MSGHEADER | ('x' << 8));

    public final int REDRANGE = 16;

    public final int BLUERANGE = 8;

    public final int GREENRANGE = 16;

    public final int GRAYSRANGE = 16;

    public final int BROWNRANGE = 16;

    public final int YELLOWRANGE = 1;

    public final int YOURRANGE = 0;

    public final int WALLRANGE = REDRANGE;

    public final int TSWALLRANGE = GRAYSRANGE;

    public final int FDWALLRANGE = BROWNRANGE;

    public final int CDWALLRANGE = YELLOWRANGE;

    public final int THINGRANGE = GREENRANGE;

    public final int SECRETWALLRANGE = WALLRANGE;

    public final int GRIDRANGE = 0;

    public boolean Responder(event_t ev);

    public void Ticker();

    public void Drawer();

    public void Stop();

    public void Start();

    public void Init();
}
