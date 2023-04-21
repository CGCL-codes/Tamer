package com.jcraft.weirdx;

import java.io.*;
import java.awt.event.KeyEvent;
import java.net.*;

final class Keymap_jp106 extends Keymap {

    private int[] _map = { 0x0, 0x0, 0x0, 0x0, 0xff1b, 0x0, 0x0, 0x0, 0x0031, 0x0021, 0x04c7, 0x00a1, 0x0032, 0x0022, 0x04cc, 0x0ac3, 0x0033, 0x0023, 0x04b1, 0x04a7, 0x0034, 0x0024, 0x04b3, 0x04a9, 0x0035, 0x0025, 0x04b4, 0x04aa, 0x0036, 0x0026, 0x04b5, 0x04ab, 0x0037, 0x0027, 0x04d4, 0x04ac, 0x0038, 0x0028, 0x04d5, 0x04ad, 0x0039, 0x0029, 0x04d6, 0x04ae, 0x0030, 0x007e, 0x04dc, 0x04a6, 0x002d, 0x003d, 0x04ce, 0x00bf, 0x005e, 0x007e, 0x04cd, 0xfe5c, 0xff08, 0x0, 0x0, 0x0, 0xff09, 0xfe20, 0x0, 0x0, 0x0071, 0x0051, 0x04c0, 0x07d9, 0x0077, 0x0057, 0x04c3, 0x01a3, 0x0065, 0x0045, 0x04b2, 0x04a8, 0x0072, 0x0052, 0x04bd, 0x00ae, 0x0074, 0x0054, 0x04b6, 0x03ac, 0x0079, 0x0059, 0x04dd, 0x00a5, 0x0075, 0x0055, 0x04c5, 0x08fc, 0x0069, 0x0049, 0x04c6, 0x02b9, 0x006f, 0x004f, 0x04d7, 0x00d8, 0x0070, 0x0050, 0x04be, 0x00de, 0x0040, 0x0060, 0x04de, 0xfe58, 0x005b, 0x007b, 0x04df, 0x04a2, 0xff0d, 0x0, 0x0, 0x0, 0xffe3, 0x0, 0x0, 0x0, 0x0061, 0x0041, 0x04c1, 0x00c6, 0x0073, 0x0053, 0x04c4, 0x00a7, 0x0064, 0x0044, 0x04bc, 0x00d0, 0x0066, 0x0046, 0x04ca, 0x00aa, 0x0067, 0x0047, 0x04b7, 0x03bd, 0x0068, 0x0048, 0x04b8, 0x02a1, 0x006a, 0x004a, 0x04cf, 0x0, 0x006b, 0x004b, 0x04c9, 0x0026, 0x006c, 0x004c, 0x04d8, 0x01a3, 0x003b, 0x002b, 0x04da, 0xfe59, 0x003a, 0x002a, 0x04b9, 0xfe5a, 0xff28, 0xff28, 0x0, 0x0, 0xffe1, 0x0, 0x0, 0x0, 0x005d, 0x007d, 0x04d1, 0x04a3, 0x007a, 0x005a, 0x04c2, 0x04af, 0x0078, 0x0058, 0x04bb, 0x003e, 0x0063, 0x0043, 0x04bf, 0x00a9, 0x0076, 0x0056, 0x04cb, 0x0060, 0x0062, 0x0042, 0x04ba, 0x0027, 0x006e, 0x004e, 0x04d0, 0x0, 0x006d, 0x004d, 0x04d3, 0x00ba, 0x002c, 0x003c, 0x04c8, 0x04a4, 0x002e, 0x003e, 0x04d9, 0x04a1, 0x002f, 0x003f, 0x04d2, 0x04a5, 0xffe2, 0x0, 0x0, 0x0, 0xffaa, 0x0, 0x0, 0x0, 0xffe9, 0xffe7, 0x0, 0x0, 0x0020, 0x0, 0x0, 0x0, 0xffff, 0xffe5, 0x0, 0x0, 0xffbe, 0x0, 0x0, 0x0, 0xffbf, 0x0, 0x0, 0x0, 0xffc0, 0x0, 0x0, 0x0, 0xffc1, 0x0, 0x0, 0x0, 0xffc2, 0x0, 0x0, 0x0, 0xffc3, 0x0, 0x0, 0x0, 0xffc4, 0x0, 0x0, 0x0, 0xffc5, 0x0, 0x0, 0x0, 0xffc6, 0x0, 0x0, 0x0, 0xffc7, 0x0, 0x0, 0x0, 0xff7e, 0x0, 0x0, 0x0, 0xff14, 0x0, 0x0, 0x0, 0xffb7, 0xffb7, 0x0, 0x0, 0xffb8, 0xffb8, 0x0, 0x0, 0xffb9, 0xffb9, 0x0, 0x0, 0xffad, 0x0, 0x0, 0x0, 0xffb4, 0xffb4, 0x0, 0x0, 0xffb5, 0xffb5, 0x0, 0x0, 0xffb6, 0xffb6, 0x0, 0x0, 0xffab, 0x0, 0x0, 0x0, 0xffb1, 0xffb1, 0x0, 0x0, 0xffb2, 0xffb2, 0x0, 0x0, 0xffb3, 0xffb3, 0x0, 0x0, 0xffb0, 0xffb0, 0x0, 0x0, 0xffae, 0xffae, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xffc8, 0x0, 0x0, 0x0, 0xffc9, 0x0, 0x0, 0x0, 0xff50, 0x0, 0x0, 0x0, 0xff52, 0x0, 0x0, 0x0, 0xff55, 0x0, 0x0, 0x0, 0xff51, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xff53, 0x0, 0x0, 0x0, 0xff57, 0x0, 0x0, 0x0, 0xff54, 0x0, 0x0, 0x0, 0xff56, 0x0, 0x0, 0x0, 0xff63, 0x0, 0x0, 0x0, 0xffff, 0x0, 0x0, 0x0, 0xff8d, 0x0, 0x0, 0x0, 0xffe4, 0x0, 0x0, 0x0, 0xff13, 0xff6b, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xffaf, 0x0, 0x0, 0x0, 0xff2a, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xff25, 0xff26, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x005c, 0x005f, 0x04db, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xff23, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xff22, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x005c, 0x007c, 0x04b0, 0x0, 0x0, 0x0, 0x0, 0x0 };

    Keymap_jp106() {
        start = 8;
        width = 4;
        count = 127;
        map = _map;
    }

    public final int getCode(KeyEvent e) {
        int key = e.getKeyCode();
        if (key != 0) {
            switch(key) {
                case KeyEvent.VK_A:
                case KeyEvent.VK_B:
                case KeyEvent.VK_C:
                case KeyEvent.VK_D:
                case KeyEvent.VK_E:
                case KeyEvent.VK_F:
                case KeyEvent.VK_G:
                case KeyEvent.VK_H:
                case KeyEvent.VK_I:
                case KeyEvent.VK_J:
                case KeyEvent.VK_K:
                case KeyEvent.VK_L:
                case KeyEvent.VK_M:
                case KeyEvent.VK_N:
                case KeyEvent.VK_O:
                case KeyEvent.VK_P:
                case KeyEvent.VK_Q:
                case KeyEvent.VK_R:
                case KeyEvent.VK_S:
                case KeyEvent.VK_T:
                case KeyEvent.VK_U:
                case KeyEvent.VK_V:
                case KeyEvent.VK_W:
                case KeyEvent.VK_X:
                case KeyEvent.VK_Y:
                case KeyEvent.VK_Z:
                    key = key + 0x20;
                    break;
                case KeyEvent.VK_0:
                case KeyEvent.VK_1:
                case KeyEvent.VK_2:
                case KeyEvent.VK_3:
                case KeyEvent.VK_4:
                case KeyEvent.VK_5:
                case KeyEvent.VK_6:
                case KeyEvent.VK_7:
                case KeyEvent.VK_8:
                case KeyEvent.VK_9:
                    break;
                case KeyEvent.VK_ENTER:
                    key = 0xff0d;
                    break;
                case KeyEvent.VK_BACK_SPACE:
                    key = 0xff08;
                    break;
                case KeyEvent.VK_TAB:
                    key = 0xff09;
                    break;
                case KeyEvent.VK_COMMA:
                case KeyEvent.VK_PERIOD:
                case KeyEvent.VK_SLASH:
                case KeyEvent.VK_OPEN_BRACKET:
                case KeyEvent.VK_CLOSE_BRACKET:
                case KeyEvent.VK_SPACE:
                    break;
                case KeyEvent.VK_SEMICOLON:
                    if (e.getKeyChar() == ':') {
                        key = 0x3a;
                    } else if (e.getKeyChar() == '*') {
                        key = 0x2a;
                    }
                    break;
                case KeyEvent.VK_EQUALS:
                    if (e.getKeyChar() == ';') {
                        key = 0x3b;
                    } else if (e.getKeyChar() == '+') {
                        key = 0x2b;
                    }
                    break;
                case KeyEvent.VK_BACK_SLASH:
                    if (e.getKeyChar() == '|') key = 0x7c;
                    break;
                case KeyEvent.VK_BACK_QUOTE:
                    key = 0x60;
                    break;
                case KeyEvent.VK_QUOTE:
                    if (e.getKeyChar() == '^') {
                        key = 0x5e;
                    } else if (e.getKeyChar() == '~') {
                        key = 0x7e;
                    } else key = 0x27;
                    break;
                case KeyEvent.VK_SHIFT:
                    key = 0xffe1;
                    break;
                case KeyEvent.VK_CONTROL:
                    key = 0xffe3;
                    break;
                case KeyEvent.VK_ALT:
                    key = 0xffe9;
                    break;
                case KeyEvent.VK_PAUSE:
                    key = 0xff13;
                    break;
                case KeyEvent.VK_CAPS_LOCK:
                    key = 0xffe5;
                    break;
                case KeyEvent.VK_ESCAPE:
                    key = 0xff1b;
                    break;
                case KeyEvent.VK_PAGE_UP:
                    key = 0xff55;
                    break;
                case KeyEvent.VK_PAGE_DOWN:
                    key = 0xff56;
                    break;
                case KeyEvent.VK_END:
                    key = 0xff57;
                    break;
                case KeyEvent.VK_HOME:
                    key = 0xff50;
                    break;
                case KeyEvent.VK_LEFT:
                    key = 0xff51;
                    break;
                case KeyEvent.VK_UP:
                    key = 0xff52;
                    break;
                case KeyEvent.VK_RIGHT:
                    key = 0xff53;
                    break;
                case KeyEvent.VK_DOWN:
                    key = 0xff54;
                    break;
                case KeyEvent.VK_NUMPAD0:
                    key = 0xffb0;
                    break;
                case KeyEvent.VK_NUMPAD1:
                    key = 0xffb1;
                    break;
                case KeyEvent.VK_NUMPAD2:
                    key = 0xffb2;
                    break;
                case KeyEvent.VK_NUMPAD3:
                    key = 0xffb3;
                    break;
                case KeyEvent.VK_NUMPAD4:
                    key = 0xffb4;
                    break;
                case KeyEvent.VK_NUMPAD5:
                    key = 0xffb5;
                    break;
                case KeyEvent.VK_NUMPAD6:
                    key = 0xffb6;
                    break;
                case KeyEvent.VK_NUMPAD7:
                    key = 0xffb7;
                    break;
                case KeyEvent.VK_NUMPAD8:
                    key = 0xffb8;
                    break;
                case KeyEvent.VK_NUMPAD9:
                    key = 0xffb9;
                    break;
                case KeyEvent.VK_MULTIPLY:
                    key = 0xffaa;
                    break;
                case KeyEvent.VK_ADD:
                    key = 0xffab;
                    break;
                case KeyEvent.VK_SEPARATER:
                    key = 0xffac;
                    break;
                case KeyEvent.VK_SUBTRACT:
                    key = 0xffad;
                    break;
                case KeyEvent.VK_DECIMAL:
                    key = 0xffae;
                    break;
                case KeyEvent.VK_DIVIDE:
                    key = 0xffaf;
                    break;
                case KeyEvent.VK_F1:
                    key = 0xffbe;
                    break;
                case KeyEvent.VK_F2:
                    key = 0xffbf;
                    break;
                case KeyEvent.VK_F3:
                    key = 0xffc0;
                    break;
                case KeyEvent.VK_F4:
                    key = 0xffc1;
                    break;
                case KeyEvent.VK_F5:
                    key = 0xffc2;
                    break;
                case KeyEvent.VK_F6:
                    key = 0xffc3;
                    break;
                case KeyEvent.VK_F7:
                    key = 0xffc4;
                    break;
                case KeyEvent.VK_F8:
                    key = 0xffc5;
                    break;
                case KeyEvent.VK_F9:
                    key = 0xffc6;
                    break;
                case KeyEvent.VK_F10:
                    key = 0xffc7;
                    break;
                case KeyEvent.VK_F11:
                    key = 0xffc8;
                    break;
                case KeyEvent.VK_F12:
                    key = 0xffc9;
                    break;
                case KeyEvent.VK_DELETE:
                    key = 0xffff;
                    break;
                case KeyEvent.VK_NUM_LOCK:
                    key = 0xff7f;
                    break;
                case KeyEvent.VK_SCROLL_LOCK:
                    key = 0xff14;
                    break;
                case KeyEvent.VK_PRINTSCREEN:
                    key = 0xff61;
                    break;
                case KeyEvent.VK_INSERT:
                    key = 0xff63;
                    break;
                case KeyEvent.VK_HELP:
                    key = 0xff6a;
                    break;
                case KeyEvent.VK_META:
                    key = 0xffe7;
                    break;
                default:
                    key = e.getKeyChar();
            }
        } else {
            key = e.getKeyChar();
        }
        int s = 10;
        if (km != null) {
            int i = 0;
            int j = 0;
            s = km.start;
            while (i < km.count * km.width) {
                if (km.map[i] == key) break;
                i++;
                j++;
                if (j == km.width) {
                    j = 0;
                    s++;
                }
            }
        }
        return s;
    }
}
