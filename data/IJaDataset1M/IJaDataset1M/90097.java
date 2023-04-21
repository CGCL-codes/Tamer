package pl.edu.agh.ssm.monitor.parsers.rtsp;

import pl.edu.agh.ssm.monitor.data.RTSPAnswer;
import pl.edu.agh.ssm.monitor.data.SessionDescription;
import pl.edu.agh.ssm.monitor.parsers.sdp.InvalidDataException;
import pl.edu.agh.ssm.monitor.parsers.sdp.SDPParser;

public class RTSPAnswerParser {

    public RTSPAnswer parse(byte[] data) {
        try {
            String str = new String(data);
            if (str.startsWith("RTSP")) {
                RTSPAnswer answer = new RTSPAnswer();
                String[] tmp = str.split("\n");
                String[] fLine = tmp[0].split(" ");
                answer.setProtocol(fLine[0]);
                answer.setAnswerCode(Integer.valueOf(fLine[1].trim()).intValue());
                answer.setAnswerText(fLine[2]);
                answer.setFullPacket(str);
                for (int i = 1; i < tmp.length; i++) {
                    if (tmp[i].startsWith("Cseq")) {
                        answer.setCSeq(Long.valueOf(tmp[i].split(":", 2)[1].trim()).longValue());
                        continue;
                    }
                    if (tmp[i].startsWith("Session")) {
                        answer.setSession(Long.valueOf(tmp[i].split(":", 2)[1].trim()).longValue());
                        continue;
                    }
                    if (tmp[i].startsWith("v=")) {
                        StringBuilder builder = new StringBuilder();
                        for (int j = i; j < tmp.length; j++) {
                            builder.append(tmp[j]);
                            builder.append("\n");
                        }
                        SDPParser parser = new SDPParser();
                        try {
                            SessionDescription desc = parser.parse(builder.toString());
                            answer.setSdp(desc);
                        } catch (InvalidDataException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
                return answer;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
