package pl.radical.open.gg;

/**
 * Created on 2005-01-29
 * 
 * @author <a href="mailto:mati@sz.home.pl">Mateusz Szczap</a>
 */
public interface ISingleChat extends IChat {

    int getRecipientUin();

    void setRecipientUin(int recipientUin);
}
