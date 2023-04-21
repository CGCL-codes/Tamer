package ca.eandb.jdcp.worker.policy.win32;

import ca.eandb.jdcp.worker.policy.win32.GDI32.RECT;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.IntByReference;

/** Provides access to the w32 user32 library.
 * Incomplete implementation to support demos.
 *
 * @author  Todd Fast, todd.fast@sun.com
 * @author twall@users.sf.net
 */
public interface User32 extends W32API {

    User32 INSTANCE = (User32) Native.loadLibrary("user32", User32.class, DEFAULT_OPTIONS);

    HDC GetDC(HWND hWnd);

    int ReleaseDC(HWND hWnd, HDC hDC);

    int FLASHW_STOP = 0;

    int FLASHW_CAPTION = 1;

    int FLASHW_TRAY = 2;

    int FLASHW_ALL = (FLASHW_CAPTION | FLASHW_TRAY);

    int FLASHW_TIMER = 4;

    int FLASHW_TIMERNOFG = 12;

    class FLASHWINFO extends Structure {

        public int cbSize;

        public HANDLE hWnd;

        public int dwFlags;

        public int uCount;

        public int dwTimeout;
    }

    int IMAGE_BITMAP = 0;

    int IMAGE_ICON = 1;

    int IMAGE_CURSOR = 2;

    int IMAGE_ENHMETAFILE = 3;

    int LR_DEFAULTCOLOR = 0x0000;

    int LR_MONOCHROME = 0x0001;

    int LR_COLOR = 0x0002;

    int LR_COPYRETURNORG = 0x0004;

    int LR_COPYDELETEORG = 0x0008;

    int LR_LOADFROMFILE = 0x0010;

    int LR_LOADTRANSPARENT = 0x0020;

    int LR_DEFAULTSIZE = 0x0040;

    int LR_VGACOLOR = 0x0080;

    int LR_LOADMAP3DCOLORS = 0x1000;

    int LR_CREATEDIBSECTION = 0x2000;

    int LR_COPYFROMRESOURCE = 0x4000;

    int LR_SHARED = 0x8000;

    HWND FindWindow(String winClass, String title);

    /** ASCII version. */
    int GetClassName(HWND hWnd, byte[] lpClassName, int nMaxCount);

    /** Unicode version. */
    int GetClassName(HWND hWnd, char[] lpClassName, int nMaxCount);

    class GUITHREADINFO extends Structure {

        public int cbSize = size();

        public int flags;

        public HWND hwndActive;

        public HWND hwndFocus;

        public HWND hwndCapture;

        public HWND hwndMenuOwner;

        public HWND hwndMoveSize;

        public HWND hwndCaret;

        public RECT rcCaret;
    }

    boolean GetGUIThreadInfo(int idThread, GUITHREADINFO lpgui);

    class WINDOWINFO extends Structure {

        public int cbSize = size();

        public RECT rcWindow;

        public RECT rcClient;

        public int dwStyle;

        public int dwExStyle;

        public int dwWindowStatus;

        public int cxWindowBorders;

        public int cyWindowBorders;

        public short atomWindowType;

        public short wCreatorVersion;
    }

    boolean GetWindowInfo(HWND hWnd, WINDOWINFO pwi);

    boolean GetWindowRect(HWND hWnd, RECT rect);

    /** ASCII version. */
    int GetWindowText(HWND hWnd, byte[] lpString, int nMaxCount);

    /** Unicode version. */
    int GetWindowText(HWND hWnd, char[] lpString, int nMaxCount);

    int GetWindowTextLength(HWND hWnd);

    /** ASCII version. */
    int GetWindowModuleFileName(HWND hWnd, byte[] lpszFileName, int cchFileNameMax);

    /** Unicode version. */
    int GetWindowModuleFileName(HWND hWnd, char[] lpszFileName, int cchFileNameMax);

    int GetWindowThreadProcessId(HWND hWnd, IntByReference lpdwProcessId);

    interface WNDENUMPROC extends StdCallCallback {

        /** Return whether to continue enumeration. */
        boolean callback(HWND hWnd, Pointer data);
    }

    boolean EnumWindows(WNDENUMPROC lpEnumFunc, Pointer data);

    boolean EnumChildWindows(HWND hWnd, WNDENUMPROC lpEnumFunc, Pointer data);

    boolean EnumThreadWindows(int dwThreadId, WNDENUMPROC lpEnumFunc, Pointer data);

    boolean FlashWindowEx(FLASHWINFO info);

    HICON LoadIcon(HINSTANCE hInstance, String iconName);

    HANDLE LoadImage(HINSTANCE hinst, String name, int type, int xDesired, int yDesired, int load);

    boolean DestroyIcon(HICON hicon);

    int GWL_EXSTYLE = -20;

    int GWL_STYLE = -16;

    int GWL_WNDPROC = -4;

    int GWL_HINSTANCE = -6;

    int GWL_ID = -12;

    int GWL_USERDATA = -21;

    int DWL_DLGPROC = 4;

    int DWL_MSGRESULT = 0;

    int DWL_USER = 8;

    int WS_EX_COMPOSITED = 0x20000000;

    int WS_EX_LAYERED = 0x80000;

    int WS_EX_TRANSPARENT = 32;

    int GetWindowLong(HWND hWnd, int nIndex);

    int SetWindowLong(HWND hWnd, int nIndex, int dwNewLong);

    int SetWindowLong(HWND hWnd, int nIndex, WindowProc dwNewLong);

    Pointer SetWindowLong(HWND hWnd, int nIndex, Pointer dwNewLong);

    LRESULT CallWindowProc(WindowProc lpPrevWndFunc, HWND hWnd, int uMsg, WPARAM wParam, LPARAM lParam);

    LRESULT CallWindowProc(int lpPrevWndFunc, HWND hWnd, int uMsg, WPARAM wParam, LPARAM lParam);

    LONG_PTR GetWindowLongPtr(HWND hWnd, int nIndex);

    LONG_PTR SetWindowLongPtr(HWND hWnd, int nIndex, LONG_PTR dwNewLongPtr);

    Pointer SetWindowLongPtr(HWND hWnd, int nIndex, Pointer dwNewLongPtr);

    int LWA_COLORKEY = 1;

    int LWA_ALPHA = 2;

    int ULW_COLORKEY = 1;

    int ULW_ALPHA = 2;

    int ULW_OPAQUE = 4;

    boolean SetLayeredWindowAttributes(HWND hwnd, int crKey, byte bAlpha, int dwFlags);

    boolean GetLayeredWindowAttributes(HWND hwnd, IntByReference pcrKey, ByteByReference pbAlpha, IntByReference pdwFlags);

    /** Defines the x- and y-coordinates of a point. */
    class POINT extends Structure {

        public int x, y;

        public POINT() {
        }

        public POINT(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    /** Specifies the width and height of a rectangle. */
    class SIZE extends Structure {

        public int cx, cy;

        public SIZE() {
        }

        public SIZE(int w, int h) {
            this.cx = w;
            this.cy = h;
        }
    }

    int AC_SRC_OVER = 0x00;

    int AC_SRC_ALPHA = 0x01;

    int AC_SRC_NO_PREMULT_ALPHA = 0x01;

    int AC_SRC_NO_ALPHA = 0x02;

    class BLENDFUNCTION extends Structure {

        public byte BlendOp = AC_SRC_OVER;

        public byte BlendFlags = 0;

        public byte SourceConstantAlpha;

        public byte AlphaFormat;
    }

    boolean UpdateLayeredWindow(HWND hwnd, HDC hdcDst, POINT pptDst, SIZE psize, HDC hdcSrc, POINT pptSrc, int crKey, BLENDFUNCTION pblend, int dwFlags);

    int SetWindowRgn(HWND hWnd, HRGN hRgn, boolean bRedraw);

    int VK_SHIFT = 16;

    int VK_LSHIFT = 0xA0;

    int VK_RSHIFT = 0xA1;

    int VK_CONTROL = 17;

    int VK_LCONTROL = 0xA2;

    int VK_RCONTROL = 0xA3;

    int VK_MENU = 18;

    int VK_LMENU = 0xA4;

    int VK_RMENU = 0xA5;

    boolean GetKeyboardState(byte[] state);

    short GetAsyncKeyState(int vKey);

    int WH_CALLWNDPROC = 4;

    int WH_CALLWNDPROCRET = 12;

    int WH_KEYBOARD = 2;

    int WH_MOUSE = 7;

    int WH_KEYBOARD_LL = 13;

    int WH_MOUSE_LL = 14;

    class HHOOK extends HANDLE {
    }

    interface HOOKPROC extends StdCallCallback {
    }

    int WM_KEYDOWN = 256;

    int WM_KEYUP = 257;

    int WM_SYSKEYDOWN = 260;

    int WM_SYSKEYUP = 261;

    int WM_POWERBROADCAST = 536;

    class KBDLLHOOKSTRUCT extends Structure {

        public int vkCode;

        public int scanCode;

        public int flags;

        public int time;

        public ULONG_PTR dwExtraInfo;
    }

    interface LowLevelKeyboardProc extends HOOKPROC {

        LRESULT callback(int nCode, WPARAM wParam, KBDLLHOOKSTRUCT lParam);
    }

    class CWPSTRUCT extends Structure {

        public LPARAM lParam;

        public WPARAM wParam;

        public int message;

        public HWND hwnd;
    }

    interface CallWndProc extends HOOKPROC {

        LRESULT callback(int nCode, WPARAM wParam, CWPSTRUCT lParam);
    }

    class CWPRETSTRUCT extends Structure {

        public LRESULT lResult;

        public LPARAM lParam;

        public WPARAM wParam;

        public int message;

        public HWND hwnd;
    }

    interface CallWndRetProc extends HOOKPROC {

        LRESULT callback(int nCode, WPARAM wParam, CWPRETSTRUCT lParam);
    }

    HHOOK SetWindowsHookEx(int idHook, HOOKPROC lpfn, HINSTANCE hMod, int dwThreadId);

    LRESULT CallNextHookEx(HHOOK hhk, int nCode, WPARAM wParam, LPARAM lParam);

    LRESULT CallNextHookEx(HHOOK hhk, int nCode, WPARAM wParam, Pointer lParam);

    boolean UnhookWindowsHookEx(HHOOK hhk);

    class MSG extends Structure {

        public HWND hWnd;

        public int message;

        public WPARAM wParam;

        public LPARAM lParam;

        public int time;

        public POINT pt;
    }

    int GetMessage(MSG lpMsg, HWND hWnd, int wMsgFilterMin, int wMsgFilterMax);

    boolean PeekMessage(MSG lpMsg, HWND hWnd, int wMsgFilterMin, int wMsgFilterMax, int wRemoveMsg);

    boolean TranslateMessage(MSG lpMsg);

    LRESULT DispatchMessage(MSG lpMsg);

    void PostMessage(HWND hWnd, int msg, WPARAM wParam, LPARAM lParam);

    void PostQuitMessage(int nExitCode);

    int PBT_APMPOWERSTATUSCHANGE = 0xA;

    int PBT_APMRESUMEAUTOMATIC = 0x12;

    int PBT_APMRESUMESUSPEND = 0x7;

    int PBT_APMSUSPEND = 0x4;

    int PBT_POWERSETTINGCHANGE = 0x8013;

    int PBT_APMBATTERYLOW = 0x9;

    int PBT_APMOEMEVENT = 0xB;

    int PBT_APMQUERYSUSPEND = 0x0;

    int PBT_APMQUERYSUSPENDFAILED = 0x2;

    int PBT_APMRESUMECRITICAL = 0x6;

    /**
	 * May be returned from WM_POWERBROADCAST in response to message with
	 * WPARAM value PBT_APMQUERYSUSPEND or PBT_APMQUERYSUSPENDFAILED
	 * (WS2003, XP, W2000).
	 */
    int BROADCAST_QUERY_DENY = 0x424D5144;

    interface WindowProc extends StdCallCallback {

        LRESULT callback(HWND hWnd, int uMsg, WPARAM wParam, LPARAM lParam);
    }

    class WNDCLASS extends Structure {

        public int style;

        public WindowProc lpfnWndProc;

        public int cbClsExtra;

        public int cbWndExtra;

        public HINSTANCE hInstance;

        public HICON hIcon;

        public HCURSOR hCursor;

        public HBRUSH hbrBackground;

        public String lpszMenuName;

        public String lpszClassName;
    }

    int CS_GLOBALCLASS = 16384;

    ATOM RegisterClass(WNDCLASS lpWndClass);

    HWND CreateWindow(char[] lpClassName, char[] lpWindowName, int dwStyle, int x, int y, int nWidth, int nHeight, HWND hWndParent, HMENU hMenu, HINSTANCE hInstance, Pointer lpParam);

    HWND CreateWindow(byte[] lpClassName, byte[] lpWindowName, int dwStyle, int x, int y, int nWidth, int nHeight, HWND hWndParent, HMENU hMenu, HINSTANCE hInstance, Pointer lpParam);

    boolean DestroyWindow(HWND hWnd);

    LRESULT DefWindowProc(HWND hWnd, int uMsg, WPARAM wParam, LPARAM lParam);

    int WS_OVERLAPPEDWINDOW = 0xCF0000;

    int CS_VREDRAW = 1;

    int CS_HREDRAW = 2;

    HWND CreateWindowEx(int dwExStyle, String lpClassName, String lpWindowName, int dwStyle, int x, int y, int nWidth, int nHeight, HWND hWndParent, HMENU hMenu, HINSTANCE hInstance, Pointer lpParam);

    int SW_SHOWDEFAULT = 10;

    boolean ShowWindow(HWND hWnd, int nCmdShow);

    boolean UpdateWindow(HWND hWnd);

    HWND GetParent(HWND hWnd);
}
