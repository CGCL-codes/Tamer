package ee.riik.xtee.naidis.producers.producer.naidis;

public interface Myport extends java.rmi.Remote {

    public void attachmentEcho(ee.riik.xtee.naidis.producers.producer.naidis.AttachmentEchoRequest keha, ee.riik.xtee.naidis.producers.producer.naidis.holders.AttachmentEchoRequestHolder paring, ee.riik.xtee.naidis.producers.producer.naidis.holders.AttachmentEchoResponseHolder keha2) throws java.rmi.RemoteException;

    public void echo(ee.riik.xtee.naidis.producers.producer.naidis.EchoRequest keha, ee.riik.xtee.naidis.producers.producer.naidis.holders.EchoRequestHolder paring, ee.riik.xtee.naidis.producers.producer.naidis.holders.EchoResponseHolder keha2) throws java.rmi.RemoteException;
}
