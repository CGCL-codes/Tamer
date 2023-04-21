package blue.ui.core.render;

import java.util.ArrayList;
import blue.Arrangement;
import blue.BlueData;
import blue.InstrumentAssignment;
import blue.automation.Automatable;
import blue.automation.Parameter;
import blue.automation.ParameterList;
import blue.mixer.Channel;
import blue.mixer.ChannelList;
import blue.mixer.EffectsChain;
import blue.mixer.Mixer;
import blue.orchestra.Instrument;

public class ParameterHelper {

    public static ArrayList getAllParameters(Arrangement arr, Mixer mixer) {
        ArrayList params = new ArrayList();
        for (int i = 0; i < arr.size(); i++) {
            InstrumentAssignment ia = arr.getInstrumentAssignment(i);
            if (ia.enabled) {
                Instrument instr = ia.instr;
                if (instr instanceof Automatable) {
                    Automatable auto = (Automatable) instr;
                    ParameterList list = auto.getParameterList();
                    params.addAll(list.getParameters());
                }
            }
        }
        if (mixer != null && mixer.isEnabled()) {
            ChannelList channels = mixer.getChannels();
            for (int i = 0; i < channels.size(); i++) {
                Channel channel = channels.getChannel(i);
                appendAllParametersFromChannel(params, channels.getChannel(i));
            }
            ChannelList subChannels = mixer.getSubChannels();
            for (int i = 0; i < subChannels.size(); i++) {
                appendAllParametersFromChannel(params, subChannels.getChannel(i));
            }
            appendAllParametersFromChannel(params, mixer.getMaster());
        }
        return params;
    }

    public static ArrayList getActiveParameters(Arrangement arr, Mixer mixer) {
        ArrayList params = new ArrayList();
        for (int i = 0; i < arr.size(); i++) {
            InstrumentAssignment ia = arr.getInstrumentAssignment(i);
            if (ia.enabled) {
                Instrument instr = ia.instr;
                if (instr instanceof Automatable) {
                    Automatable auto = (Automatable) instr;
                    ParameterList list = auto.getParameterList();
                    addActiveParametersFromList(params, list);
                }
            }
        }
        if (mixer != null && mixer.isEnabled()) {
            ChannelList channels = mixer.getChannels();
            for (int i = 0; i < channels.size(); i++) {
                appendParametersFromChannel(params, channels.getChannel(i));
            }
            ChannelList subChannels = mixer.getSubChannels();
            for (int i = 0; i < subChannels.size(); i++) {
                appendParametersFromChannel(params, subChannels.getChannel(i));
            }
            appendParametersFromChannel(params, mixer.getMaster());
        }
        return params;
    }

    private static void appendAllParametersFromChannel(ArrayList params, Channel channel) {
        EffectsChain pre = channel.getPreEffects();
        for (int i = 0; i < pre.size(); i++) {
            ParameterList list = ((Automatable) pre.getElementAt(i)).getParameterList();
            params.addAll(list.getParameters());
        }
        EffectsChain post = channel.getPostEffects();
        for (int i = 0; i < post.size(); i++) {
            ParameterList list = ((Automatable) post.getElementAt(i)).getParameterList();
            params.addAll(list.getParameters());
        }
        Parameter levelParameter = channel.getLevelParameter();
        params.add(levelParameter);
    }

    private static void appendParametersFromChannel(ArrayList params, Channel channel) {
        EffectsChain pre = channel.getPreEffects();
        for (int i = 0; i < pre.size(); i++) {
            ParameterList list = ((Automatable) pre.getElementAt(i)).getParameterList();
            addActiveParametersFromList(params, list);
        }
        EffectsChain post = channel.getPostEffects();
        for (int i = 0; i < post.size(); i++) {
            ParameterList list = ((Automatable) post.getElementAt(i)).getParameterList();
            addActiveParametersFromList(params, list);
        }
        if (channel.getLevelParameter().isAutomationEnabled()) {
            params.add(channel.getLevelParameter());
        }
    }

    private static void addActiveParametersFromList(ArrayList params, ParameterList list) {
        for (int j = 0; j < list.size(); j++) {
            Parameter param = list.getParameter(j);
            if (param.isAutomationEnabled()) {
                params.add(param);
            }
        }
    }

    public static void clearCompilationVarNames(BlueData data) {
        Arrangement arrangement = data.getArrangement();
        for (int i = 0; i < arrangement.size(); i++) {
            Instrument instr = arrangement.getInstrument(i);
            if (instr instanceof Automatable) {
                Automatable temp = (Automatable) instr;
                temp.getParameterList().clearCompilationVarNames();
            }
        }
        Mixer mixer = data.getMixer();
        ChannelList channels = mixer.getChannels();
        for (int i = 0; i < channels.size(); i++) {
            clearChannelCompilationVar(channels.getChannel(i));
        }
        ChannelList subChannels = mixer.getSubChannels();
        for (int i = 0; i < subChannels.size(); i++) {
            clearChannelCompilationVar(subChannels.getChannel(i));
        }
        clearChannelCompilationVar(mixer.getMaster());
    }

    private static void clearChannelCompilationVar(Channel channel) {
        channel.getLevelParameter().setCompilationVarName(null);
        EffectsChain preEffectsChain = channel.getPreEffects();
        for (int j = 0; j < preEffectsChain.size(); j++) {
            Automatable automatable = (Automatable) preEffectsChain.getElementAt(j);
            automatable.getParameterList().clearCompilationVarNames();
        }
        EffectsChain postEffectsChain = channel.getPostEffects();
        for (int j = 0; j < postEffectsChain.size(); j++) {
            Automatable automatable = (Automatable) postEffectsChain.getElementAt(j);
            automatable.getParameterList().clearCompilationVarNames();
        }
    }
}
