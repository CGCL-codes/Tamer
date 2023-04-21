package sdl.event;

import sdl.utils.Debug;

public class SDLEvent {

    public byte m_TypeEvent;

    public static final int SDL_NOEVENT = 0;

    public static final int SDL_ACTIVEEVENT = 1;

    public static final int SDL_KEYDOWN = 2;

    public static final int SDL_KEYUP = 3;

    public static final int SDL_MOUSEMOTION = 4;

    public static final int SDL_MOUSEBUTTONDOWN = 5;

    public static final int SDL_MOUSEBUTTONUP = 6;

    public static final int SDL_JOYAXISMOTION = 7;

    public static final int SDL_JOYBALLMOTION = 8;

    public static final int SDL_JOYHATMOTION = 9;

    public static final int SDL_JOYBUTTONDOWN = 10;

    public static final int SDL_JOYBUTTONUP = 11;

    public static final int SDL_QUIT = 12;

    public static final int SDL_SYSWMEVENT = 13;

    public static final int SDL_EVENT_RESERVEDA = 14;

    public static final int SDL_EVENT_RESERVEDB = 15;

    public static final int SDL_VIDEORESIZE = 16;

    public static final int SDL_EVENT_RESERVED1 = 17;

    public static final int SDL_EVENT_RESERVED2 = 18;

    public static final int SDL_EVENT_RESERVED3 = 19;

    public static final int SDL_EVENT_RESERVED4 = 20;

    public static final int SDL_EVENT_RESERVED5 = 21;

    public static final int SDL_EVENT_RESERVED6 = 22;

    public static final int SDL_EVENT_RESERVED7 = 23;

    public static final int SDL_USEREVENT = 24;

    public static final int SDL_NUMEVENTS = 32;

    public static final int SDL_QUERY = -1;

    public static final int SDL_IGNORE = 0;

    public static final int SDL_ENABLE = 1;

    private int m_Handle;

    private static final boolean DBG = true;

    public static SDLCustomEvent createSDLEvent(int handle) {
        SDLCustomEvent event = null;
        int tmp = getEventType(handle);
        switch(tmp) {
            case SDL_NOEVENT:
                event = new SDLCustomEvent(handle);
                event.setType((byte) SDL_NOEVENT);
                break;
            case SDL_ACTIVEEVENT:
                break;
            case SDL_KEYDOWN:
                event = new SDLKeyboardEvent(handle);
                break;
            case SDL_KEYUP:
                event = new SDLKeyboardEvent(handle);
                break;
            case SDL_MOUSEMOTION:
                event = new SDLMouseMotionEvent(handle);
                break;
            case SDL_MOUSEBUTTONDOWN:
                event = new SDLMouseButtonEvent(handle);
                break;
            case SDL_MOUSEBUTTONUP:
                event = new SDLMouseButtonEvent(handle);
                break;
            case SDL_JOYAXISMOTION:
                break;
            case SDL_JOYBALLMOTION:
                break;
            case SDL_JOYHATMOTION:
                break;
            case SDL_JOYBUTTONDOWN:
                break;
            case SDL_JOYBUTTONUP:
                break;
            case SDL_QUIT:
                event = new SDLQuitEvent(handle);
                break;
            case SDL_SYSWMEVENT:
                break;
            case SDL_EVENT_RESERVEDA:
                break;
            case SDL_EVENT_RESERVEDB:
                break;
            case SDL_VIDEORESIZE:
                break;
            case SDL_EVENT_RESERVED1:
                break;
            case SDL_EVENT_RESERVED2:
                break;
            case SDL_EVENT_RESERVED3:
                break;
            case SDL_EVENT_RESERVED4:
                break;
            case SDL_EVENT_RESERVED5:
                break;
            case SDL_EVENT_RESERVED6:
                break;
            case SDL_EVENT_RESERVED7:
                break;
            case SDL_USEREVENT:
                event = new SDLUserEvent(handle);
                break;
            case SDL_NUMEVENTS:
                break;
        }
        return event;
    }

    private static native int getEventType(int handle);
}
