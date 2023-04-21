package eisbot.abl.java;

import abl.runtime.*;
import wm.WME;
import java.util.*;
import java.lang.reflect.Method;
import abl.learning.*;
import eisbot.proxy.*;
import eisbot.proxy.wmes.*;
import eisbot.buildorder.*;
import eisbot.proxy.wmes.unit.*;
import eisbot.proxy.wmes.unit.protoss.*;
import eisbot.proxy.filter.*;
import eisbot.abl.*;
import eisbot.abl.actions.*;
import eisbot.abl.sensors.*;
import eisbot.abl.wmes.*;
import eisbot.abl.wmes.requests.*;
import eisbot.abl.wmes.prediction.*;
import java.awt.Point;

public class EISBot_ArgumentStepExecute implements eisbot.abl.StarCraftConstants {

    public static Object[] argumentExecute0(int __$stepID, final Object[] __$behaviorFrame, final BehavingEntity __$thisEntity) {
        switch(__$stepID) {
            case 3:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(120);
                    return args;
                }
            case 27:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(24);
                    return args;
                }
            case 29:
                {
                    final Object[] args = new Object[2];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    return args;
                }
            case 30:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(24);
                    return args;
                }
            case 32:
                {
                    final Object[] args = new Object[2];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    return args;
                }
            case 33:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(24);
                    return args;
                }
            case 34:
                {
                    final Object[] args = new Object[2];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[2]).i);
                    args[1] = new Integer(13);
                    return args;
                }
            case 35:
                {
                    final Object[] args = new Object[2];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[2]).i);
                    args[1] = new Integer(5);
                    return args;
                }
            case 36:
                {
                    final Object[] args = new Object[2];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[2]).i);
                    args[1] = new Integer(15);
                    return args;
                }
            case 39:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(240);
                    return args;
                }
            case 42:
                {
                    final Object[] args = new Object[2];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[4]).i);
                    args[1] = new Integer(67);
                    return args;
                }
            case 43:
                {
                    final Object[] args = new Object[2];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[4]).i);
                    args[1] = new Integer(66);
                    return args;
                }
            case 44:
                {
                    final Object[] args = new Object[2];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[4]).i);
                    args[1] = new Integer(65);
                    return args;
                }
            case 48:
                {
                    final Object[] args = new Object[2];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    args[1] = new Integer(19);
                    return args;
                }
            case 49:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(6);
                    return args;
                }
            case 50:
                {
                    final Object[] args = new Object[2];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    args[1] = new Integer(19);
                    return args;
                }
            case 51:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(6);
                    return args;
                }
            case 52:
                {
                    final Object[] args = new Object[2];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    args[1] = new Integer(19);
                    return args;
                }
            case 54:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(7200);
                    return args;
                }
            case 57:
                {
                    final Object[] args = new Object[2];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    args[1] = new Integer(40);
                    return args;
                }
            case 59:
                {
                    final Object[] args = new Object[2];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    args[1] = new Integer(34);
                    return args;
                }
            case 61:
                {
                    final Object[] args = new Object[2];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    args[1] = new Integer(33);
                    return args;
                }
            case 63:
                {
                    final Object[] args = new Object[2];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    args[1] = new Integer(43);
                    return args;
                }
            case 65:
                {
                    final Object[] args = new Object[2];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    args[1] = new Integer(38);
                    return args;
                }
            case 66:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(7200);
                    return args;
                }
            case 69:
                {
                    final Object[] args = new Object[2];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    args[1] = new Integer(39);
                    return args;
                }
            case 74:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(48);
                    return args;
                }
            case 77:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(48);
                    return args;
                }
            case 80:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(48);
                    return args;
                }
            case 83:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(240);
                    return args;
                }
            case 86:
                {
                    final Object[] args = new Object[2];
                    args[0] = ((ProbeWME) __$behaviorFrame[0]);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[2]).i);
                    return args;
                }
            case 88:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Boolean(((__ValueTypes.BooleanVar) __$behaviorFrame[4]).b);
                    return args;
                }
            case 89:
                {
                    final Object[] args = new Object[3];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[2]).i);
                    args[2] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[3]).i);
                    return args;
                }
            case 90:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(48);
                    return args;
                }
            case 93:
                {
                    final Object[] args = new Object[3];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[2]).i);
                    args[2] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[3]).i);
                    return args;
                }
            case 94:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(2);
                    return args;
                }
            case 97:
                {
                    final Object[] args = new Object[3];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[3]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    args[2] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[2]).i);
                    return args;
                }
            case 98:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(150);
                    return args;
                }
            case 100:
                {
                    final Object[] args = new Object[3];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[3]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[6]).i);
                    args[2] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[7]).i);
                    return args;
                }
            case 101:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(2);
                    return args;
                }
            case 107:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(48);
                    return args;
                }
            case 110:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(48);
                    return args;
                }
            case 116:
                {
                    final Object[] args = new Object[1];
                    args[0] = ((RequestWME) __$behaviorFrame[1]);
                    return args;
                }
            case 126:
                {
                    final Object[] args = new Object[4];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[2]).i);
                    args[2] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[3]).i);
                    args[3] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[4]).i);
                    return args;
                }
            case 127:
                {
                    final Object[] args = new Object[2];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[3]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[4]).i);
                    return args;
                }
            case 128:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(6);
                    return args;
                }
            case 144:
                {
                    final Object[] args = new Object[3];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[2]).i);
                    args[2] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[3]).i);
                    return args;
                }
            case 145:
                {
                    final Object[] args = new Object[2];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[2]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[3]).i);
                    return args;
                }
            case 146:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(12);
                    return args;
                }
            case 154:
                {
                    final Object[] args = new Object[5];
                    args[0] = new Boolean(((__ValueTypes.BooleanVar) __$behaviorFrame[3]).b);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    args[2] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[2]).i);
                    args[3] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[4]).i);
                    args[4] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[5]).i);
                    return args;
                }
            case 155:
                {
                    final Object[] args = new Object[2];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[2]).i);
                    args[1] = new Integer(-1);
                    return args;
                }
            case 156:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(12);
                    return args;
                }
            case 159:
                {
                    final Object[] args = new Object[2];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[5]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    return args;
                }
            case 161:
                {
                    final Object[] args = new Object[2];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[6]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    return args;
                }
            case 163:
                {
                    final Object[] args = new Object[2];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[5]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    return args;
                }
            case 165:
                {
                    final Object[] args = new Object[2];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[6]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    return args;
                }
            case 167:
                {
                    final Object[] args = new Object[1];
                    args[0] = ((ArchonRequestWME) __$behaviorFrame[0]);
                    return args;
                }
            case 168:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(6);
                    return args;
                }
            case 171:
                {
                    final Object[] args = new Object[3];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[8]).i);
                    args[1] = new Integer(23);
                    args[2] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[6]).i);
                    return args;
                }
            case 172:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(6);
                    return args;
                }
            case 175:
                {
                    final Object[] args = new Object[3];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[3]).i);
                    args[1] = new Integer(23);
                    args[2] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[4]).i);
                    return args;
                }
            case 176:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(6);
                    return args;
                }
            case 177:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(6);
                    return args;
                }
            case 183:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(48);
                    return args;
                }
            case 184:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    return args;
                }
            case 186:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    return args;
                }
            case 187:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(24);
                    return args;
                }
            case 189:
                {
                    final Object[] args = new Object[2];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    args[1] = new Integer(84);
                    return args;
                }
            case 190:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(120);
                    return args;
                }
            case 193:
                {
                    final Object[] args = new Object[2];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[4]).i);
                    return args;
                }
            case 194:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(24);
                    return args;
                }
            case 197:
                {
                    final Object[] args = new Object[2];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[4]).i);
                    return args;
                }
            case 198:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(24);
                    return args;
                }
            case 200:
                {
                    final Object[] args = new Object[2];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    return args;
                }
            case 219:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(24);
                    return args;
                }
            case 222:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(24);
                    return args;
                }
            case 225:
                {
                    final Object[] args = new Object[2];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    return args;
                }
            case 226:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(750);
                    return args;
                }
            case 231:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(24);
                    return args;
                }
            case 233:
                {
                    final Object[] args = new Object[2];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[3]).i);
                    args[1] = new Integer(64);
                    return args;
                }
            case 234:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(24);
                    return args;
                }
            case 236:
                {
                    final Object[] args = new Object[2];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[3]).i);
                    args[1] = new Integer(64);
                    return args;
                }
            case 237:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(24);
                    return args;
                }
            case 239:
                {
                    final Object[] args = new Object[2];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[3]).i);
                    args[1] = new Integer(64);
                    return args;
                }
            case 240:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(24);
                    return args;
                }
            case 243:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(24);
                    return args;
                }
            case 246:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(2);
                    return args;
                }
            case 249:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(500);
                    return args;
                }
            case 253:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(2);
                    return args;
                }
            case 256:
                {
                    final Object[] args = new Object[5];
                    args[0] = ((ProbeWME) __$behaviorFrame[7]);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[2]).i);
                    args[2] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[4]).i);
                    args[3] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[5]).i);
                    args[4] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[6]).i);
                    return args;
                }
            case 257:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(2);
                    return args;
                }
            case 258:
                {
                    final Object[] args = new Object[5];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    args[2] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[4]).i);
                    args[3] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[5]).i);
                    args[4] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[8]).i);
                    return args;
                }
            case 259:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(48);
                    return args;
                }
            case 261:
                {
                    final Object[] args = new Object[5];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    args[2] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[3]).i);
                    args[3] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[4]).i);
                    args[4] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[5]).i);
                    return args;
                }
            case 262:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(120);
                    return args;
                }
            case 264:
                {
                    final Object[] args = new Object[5];
                    args[0] = ((ProbeWME) __$behaviorFrame[5]);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[9]).i);
                    args[2] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[2]).i);
                    args[3] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[3]).i);
                    args[4] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[10]).i);
                    return args;
                }
            case 265:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(2);
                    return args;
                }
            case 266:
                {
                    final Object[] args = new Object[5];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    args[2] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[2]).i);
                    args[3] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[3]).i);
                    args[4] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[4]).i);
                    return args;
                }
            case 267:
                {
                    final Object[] args = new Object[3];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[2]).i);
                    args[2] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[3]).i);
                    return args;
                }
            case 268:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(200);
                    return args;
                }
            case 269:
                {
                    final Object[] args = new Object[3];
                    args[0] = ((ProbeWME) __$behaviorFrame[0]);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    args[2] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[4]).i);
                    return args;
                }
            case 270:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(2);
                    return args;
                }
            case 271:
                {
                    final Object[] args = new Object[2];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[4]).i);
                    return args;
                }
            case 272:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(2);
                    return args;
                }
            case 274:
                {
                    final Object[] args = new Object[2];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[2]).i);
                    return args;
                }
            case 275:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(2);
                    return args;
                }
            case 278:
                {
                    final Object[] args = new Object[5];
                    args[0] = ((NexusWME) __$behaviorFrame[3]);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    args[2] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    args[3] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[2]).i);
                    args[4] = new Integer(5);
                    return args;
                }
            case 279:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(100);
                    return args;
                }
            case 281:
                {
                    final Object[] args = new Object[2];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[8]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[9]).i);
                    return args;
                }
            case 282:
                {
                    final Object[] args = new Object[5];
                    args[0] = ((NexusWME) __$behaviorFrame[0]);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    args[2] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[2]).i);
                    args[3] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[3]).i);
                    args[4] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[4]).i);
                    return args;
                }
            case 284:
                {
                    final Object[] args = new Object[4];
                    args[0] = ((ProbeWME) __$behaviorFrame[3]);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    args[2] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    args[3] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[2]).i);
                    return args;
                }
            case 287:
                {
                    final Object[] args = new Object[2];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[9]).i);
                    return args;
                }
            case 289:
                {
                    final Object[] args = new Object[2];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[9]).i);
                    return args;
                }
            case 291:
                {
                    final Object[] args = new Object[6];
                    args[0] = ((AssimilatorWME) __$behaviorFrame[0]);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    args[2] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[4]).i);
                    args[3] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[5]).i);
                    args[4] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[2]).i);
                    args[5] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[3]).i);
                    return args;
                }
            case 292:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(48);
                    return args;
                }
            case 294:
                {
                    final Object[] args = new Object[2];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[7]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    return args;
                }
            case 296:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(24);
                    return args;
                }
            case 306:
                {
                    final Object[] args = new Object[2];
                    args[0] = ((ConstructionWME) __$behaviorFrame[0]);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    return args;
                }
            case 310:
                {
                    final Object[] args = new Object[1];
                    args[0] = ((ConstructionWME) __$behaviorFrame[0]);
                    return args;
                }
            case 311:
                {
                    final Object[] args = new Object[1];
                    args[0] = ((ConstructionWME) __$behaviorFrame[0]);
                    return args;
                }
            case 314:
                {
                    final Object[] args = new Object[2];
                    args[0] = ((ConstructionWME) __$behaviorFrame[0]);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[2]).i);
                    return args;
                }
            case 315:
                {
                    final Object[] args = new Object[1];
                    args[0] = ((ConstructionWME) __$behaviorFrame[0]);
                    return args;
                }
            case 316:
                {
                    final Object[] args = new Object[4];
                    args[0] = ((ConstructionWME) __$behaviorFrame[0]);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    args[2] = ((ProbeWME) __$behaviorFrame[2]);
                    args[3] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[3]).i);
                    return args;
                }
            case 317:
                {
                    final Object[] args = new Object[4];
                    args[0] = ((ConstructionWME) __$behaviorFrame[0]);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    args[2] = ((ProbeWME) __$behaviorFrame[2]);
                    args[3] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[3]).i);
                    return args;
                }
            case 319:
                {
                    final Object[] args = new Object[3];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    args[1] = ((ProbeWME) __$behaviorFrame[2]);
                    args[2] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[3]).i);
                    return args;
                }
            case 320:
                {
                    final Object[] args = new Object[1];
                    args[0] = ((ConstructionWME) __$behaviorFrame[0]);
                    return args;
                }
            case 322:
                {
                    final Object[] args = new Object[2];
                    args[0] = ((ProbeWME) __$behaviorFrame[2]);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[3]).i);
                    return args;
                }
            case 323:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(360);
                    return args;
                }
            case 325:
                {
                    final Object[] args = new Object[5];
                    args[0] = ((ProbeWME) __$behaviorFrame[0]);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    args[2] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[2]).i);
                    args[3] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[3]).i);
                    args[4] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[4]).i);
                    return args;
                }
            case 326:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(360);
                    return args;
                }
            case 330:
                {
                    final Object[] args = new Object[3];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[2]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[4]).i);
                    args[2] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[3]).i);
                    return args;
                }
            case 331:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(6);
                    return args;
                }
            case 332:
                {
                    final Object[] args = new Object[4];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[2]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[4]).i);
                    args[2] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[3]).i);
                    args[3] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    return args;
                }
            case 333:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(48);
                    return args;
                }
            case 334:
                {
                    final Object[] args = new Object[4];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    args[2] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[2]).i);
                    args[3] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[3]).i);
                    return args;
                }
            case 368:
                {
                    final Object[] args = new Object[3];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[4]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    args[2] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    return args;
                }
            case 369:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(4);
                    return args;
                }
            case 371:
                {
                    final Object[] args = new Object[3];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[3]).i);
                    args[2] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[4]).i);
                    return args;
                }
            case 372:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(24);
                    return args;
                }
            case 374:
                {
                    final Object[] args = new Object[2];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[5]).i);
                    return args;
                }
            case 375:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(24);
                    return args;
                }
            case 378:
                {
                    final Object[] args = new Object[3];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[4]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    args[2] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    return args;
                }
            case 379:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(24);
                    return args;
                }
            case 382:
                {
                    final Object[] args = new Object[3];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[4]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    args[2] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    return args;
                }
            case 383:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(24);
                    return args;
                }
            case 386:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(100);
                    return args;
                }
            case 387:
                {
                    final Object[] args = new Object[3];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    args[2] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[2]).i);
                    return args;
                }
            case 389:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    return args;
                }
            case 390:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    return args;
                }
            case 391:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(120);
                    return args;
                }
            case 396:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    return args;
                }
            case 397:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    return args;
                }
            case 400:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(24);
                    return args;
                }
            case 402:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    return args;
                }
            case 403:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(24);
                    return args;
                }
            case 404:
                {
                    final Object[] args = new Object[3];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[4]).i);
                    args[2] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[5]).i);
                    return args;
                }
            case 405:
                {
                    final Object[] args = new Object[3];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[4]).i);
                    args[2] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[3]).i);
                    return args;
                }
            case 406:
                {
                    final Object[] args = new Object[3];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[4]).i);
                    args[2] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[3]).i);
                    return args;
                }
            case 407:
                {
                    final Object[] args = new Object[3];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[4]).i);
                    args[2] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[3]).i);
                    return args;
                }
            case 408:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(48);
                    return args;
                }
            case 413:
                {
                    final Object[] args = new Object[2];
                    args[0] = ((SquadWME) __$behaviorFrame[0]);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[3]).i);
                    return args;
                }
            case 414:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(24);
                    return args;
                }
            case 417:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(100);
                    return args;
                }
            case 418:
                {
                    final Object[] args = new Object[3];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    args[2] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[2]).i);
                    return args;
                }
            case 420:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    return args;
                }
            case 421:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(48);
                    return args;
                }
            case 423:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(48);
                    return args;
                }
            case 424:
                {
                    final Object[] args = new Object[2];
                    args[0] = ((SquadWME) __$behaviorFrame[0]);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    return args;
                }
            case 429:
                {
                    final Object[] args = new Object[3];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    args[2] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[2]).i);
                    return args;
                }
            case 430:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(2);
                    return args;
                }
            case 433:
                {
                    final Object[] args = new Object[3];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    args[2] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[2]).i);
                    return args;
                }
            case 434:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(48);
                    return args;
                }
            case 437:
                {
                    final Object[] args = new Object[3];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    args[2] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[2]).i);
                    return args;
                }
            case 439:
                {
                    final Object[] args = new Object[2];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    return args;
                }
            case 440:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(12);
                    return args;
                }
            case 442:
                {
                    final Object[] args = new Object[2];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    return args;
                }
            case 443:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(24);
                    return args;
                }
            case 446:
                {
                    final Object[] args = new Object[3];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[2]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    args[2] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    return args;
                }
            case 447:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(48);
                    return args;
                }
            case 450:
                {
                    final Object[] args = new Object[3];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[2]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    args[2] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    return args;
                }
            case 451:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(48);
                    return args;
                }
            case 453:
                {
                    final Object[] args = new Object[3];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[4]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    args[2] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    return args;
                }
            case 454:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(2);
                    return args;
                }
            case 456:
                {
                    final Object[] args = new Object[3];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[4]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    args[2] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    return args;
                }
            case 457:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(2);
                    return args;
                }
            case 460:
                {
                    final Object[] args = new Object[2];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    return args;
                }
            case 461:
                {
                    final Object[] args = new Object[3];
                    args[0] = ((DragoonWME) __$behaviorFrame[3]);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    args[2] = new Integer(24);
                    return args;
                }
            case 462:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(1);
                    return args;
                }
            case 465:
                {
                    final Object[] args = new Object[2];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    return args;
                }
            case 466:
                {
                    final Object[] args = new Object[3];
                    args[0] = ((DragoonWME) __$behaviorFrame[3]);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    args[2] = new Integer(24);
                    return args;
                }
            case 467:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(1);
                    return args;
                }
            case 468:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[2]).i);
                    return args;
                }
            case 470:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    return args;
                }
            case 471:
                {
                    final Object[] args = new Object[3];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[2]).i);
                    args[2] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    return args;
                }
            case 472:
                {
                    final Object[] args = new Object[3];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[2]).i);
                    args[2] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    return args;
                }
            case 475:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(12);
                    return args;
                }
            case 478:
                {
                    final Object[] args = new Object[3];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[4]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[5]).i);
                    args[2] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[6]).i);
                    return args;
                }
            case 479:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(12);
                    return args;
                }
            case 482:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(48);
                    return args;
                }
            case 484:
                {
                    final Object[] args = new Object[2];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[4]).i);
                    return args;
                }
            case 485:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(12);
                    return args;
                }
            case 488:
                {
                    final Object[] args = new Object[3];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[3]).i);
                    args[1] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    args[2] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[2]).i);
                    return args;
                }
            case 489:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(12);
                    return args;
                }
            case 491:
                {
                    final Object[] args = new Object[3];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    args[1] = new Integer(19);
                    args[2] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    return args;
                }
            case 492:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(24);
                    return args;
                }
            case 495:
                {
                    final Object[] args = new Object[3];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[2]).i);
                    args[1] = new Integer(23);
                    args[2] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[3]).i);
                    return args;
                }
            case 496:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(6);
                    return args;
                }
            case 497:
                {
                    final Object[] args = new Object[2];
                    args[0] = ((HighTemplarWME) __$behaviorFrame[0]);
                    args[1] = ((HighTemplarWME) __$behaviorFrame[1]);
                    return args;
                }
            case 498:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(240);
                    return args;
                }
            case 501:
                {
                    final Object[] args = new Object[2];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[0]).i);
                    args[1] = new Integer(73);
                    return args;
                }
            case 502:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(48);
                    return args;
                }
            case 507:
                {
                    final Object[] args = new Object[1];
                    args[0] = new Integer(((__ValueTypes.IntVar) __$behaviorFrame[1]).i);
                    return args;
                }
            default:
                throw new AblRuntimeError("Unexpected stepID " + __$stepID);
        }
    }
}
