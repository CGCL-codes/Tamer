package br.com.visualmidia.ui.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import br.com.visualmidia.business.Photo;
import br.com.visualmidia.system.GDRegistry;
import br.com.visualmidia.system.GDSystem;
import br.com.visualmidia.ui.wizard.composite.LogoComposite;

/**
 * @author  Lucas
 */
public class LoadLogo extends WizardPage {

    private GDRegistry gdRegistry;

    private LogoComposite composite;

    public LoadLogo() {
        super("Dados");
        setTitle("Dados da Empresa");
        setDescription("Carregue o logo da sua empresa:");
        gdRegistry = GDSystem.getGDRegistry();
        setImageDescriptor(gdRegistry.getImageDescriptor(GDRegistry.IMAGE_VM));
    }

    public void createControl(Composite parent) {
        composite = new LogoComposite(parent, SWT.NONE);
        createLegend();
        setControl(composite);
    }

    private void createLegend() {
        Label legend = new Label(composite, SWT.NONE);
        legend.setText("Obs: O logotipo deve conter um tamanho de no m�ximo 362x198 e no m�ximo 500 KB de tamanho.");
    }

    public Photo getPhoto() {
        return composite.getPhoto();
    }
}
