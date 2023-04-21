package br.com.aulaweb.persistence.antigo;

import br.com.aulaweb.resource.Image;
import br.com.aulaweb.resource.RichText;
import br.com.aulaweb.resource.problem.objectiveproblem.ObjectiveProblemManager;
import org.odmg.Implementation;
import org.odmg.Transaction;

/**
 * Insert the type's description here.
 * Creation date: (04.03.2001 10:34:15)
 * @author: Administrator
 */
public class UCEnterNewProduct extends AbstractUseCase {

    public UCEnterNewProduct(Implementation impl) {
        super(impl);
    }

    /** perform this use case*/
    public void apply() {
        try {
            RichText newRichText = new RichText();
            newRichText.setText("Apenas um teste");
            Image im = new Image(newRichText);
            im.setTitle("imagem1");
            im.setDescription("Engenharia do Dominio");
            im.setHeight(100);
            im.setWidth(100);
            String fileName = "badday.txt";
            byte[] bytes = "Apenas um teste.".getBytes();
            ObjectiveProblemManager.getInstance().insertImage(im, fileName, bytes);
            Transaction tx = null;
            tx = odmg.newTransaction();
            tx.begin();
            tx.lock(newRichText, tx.WRITE);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** get descriptive information on use case*/
    public String getDescription() {
        return "Enter a new RichText";
    }
}
