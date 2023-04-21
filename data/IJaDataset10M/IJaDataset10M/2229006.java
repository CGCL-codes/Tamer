package jp.seraph.robocup.soccer.research.sample.agent;

import java.io.IOException;
import java.net.InetSocketAddress;
import jp.seraph.jsade.net.DefaultRoboCupSocket;
import jp.seraph.jsade.net.RoboCupSocket;

public class SampleAgentRunner {

    /**
     * @param args
     */
    public static void main(String[] args) {
        RoboCupSocket tSocket = null;
        try {
            tSocket = new DefaultRoboCupSocket();
            tSocket.connect(new InetSocketAddress("127.0.0.1", 3100));
        } catch (IOException e) {
            System.out.println("サーバーへの接続に失敗しました。");
            e.printStackTrace();
            return;
        }
        SampleNaoAgent tAgent = new SampleNaoAgent(tSocket);
        tAgent.run();
    }
}
