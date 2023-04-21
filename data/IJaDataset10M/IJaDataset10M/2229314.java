package dados;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import logica.emulador;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import com.thoughtworks.selenium.Selenium;

public class SCA extends Thread {

    public HtmlUnitDriver driver;

    public Selenium selenium;

    public SCA(HtmlUnitDriver driver, Selenium selenium) {
        this.driver = driver;
        this.selenium = selenium;
    }

    public String getSemestre(emulador servidor) {
        servidor.selenium.open("/sistemas/contac/index_contac.php");
        String semestre = servidor.selenium.getText("//tr[3]/td[3]/span[2]");
        return semestre;
    }

    public void setAnoSimestre(emulador servidor, String ano, String semestre) {
        servidor.selenium.open("/sistemas/contac/escolher_ano_semestre2.php");
        WebElement element = servidor.driver.findElement(By.xpath("//option[@value='" + ano + "']"));
        element.setSelected();
        WebElement element2 = servidor.driver.findElement(By.xpath("//option[@value='" + semestre + "']"));
        element2.setSelected();
        servidor.selenium.click("B1");
    }

    public ArrayList<String> getDisciplinaNota(emulador servidor, String ano, String semestre, int modoVisualizacao) {
        int i = 1;
        ArrayList<String> retorno = new ArrayList<String>();
        while (servidor.selenium.isElementPresent("//tr[" + i + "]/td[2]/a")) {
            String txt = servidor.selenium.getText("//tr[" + i + "]/td[2]/a");
            retorno.add(txt);
            WebElement element3 = servidor.driver.findElement(By.partialLinkText(txt));
            element3.click();
            servidor.selenium.waitForPageToLoad("30000");
            WebElement element7;
            if (modoVisualizacao == 1) {
                int l = 1;
                while (servidor.selenium.isElementPresent("//tr[3]/td[" + l + "]/div")) {
                    String txt2 = servidor.selenium.getText("//tr[3]/td[" + l + "]/div");
                    retorno.add(txt2);
                    l++;
                }
            } else {
                String txt2 = servidor.selenium.getText("//tr[3]/td[9]/div");
                retorno.add(txt2);
            }
            i++;
            servidor.selenium.waitForPageToLoad("30000");
            setAnoSimestre(servidor, ano, semestre);
            servidor.selenium.waitForPageToLoad("30000");
        }
        int j = 1;
        while (servidor.selenium.isElementPresent("//tr[" + j + "]/td[4]/a")) {
            String txt = servidor.selenium.getText("//tr[" + j + "]/td[4]/a");
            retorno.add(txt);
            WebElement element3 = servidor.driver.findElement(By.partialLinkText(txt));
            element3.click();
            servidor.selenium.waitForPageToLoad("30000");
            WebElement element7;
            if (modoVisualizacao == 1) {
                int m = 1;
                while (servidor.selenium.isElementPresent("//tr[3]/td[" + m + "]/div")) {
                    String txt2 = servidor.selenium.getText("//tr[3]/td[" + m + "]/div");
                    retorno.add(txt2);
                    m++;
                }
            } else {
                String txt2 = servidor.selenium.getText("//tr[3]/td[9]/div");
                retorno.add(txt2);
            }
            j++;
            servidor.selenium.waitForPageToLoad("30000");
            setAnoSimestre(servidor, ano, semestre);
            servidor.selenium.waitForPageToLoad("30000");
        }
        return retorno;
    }

    public void autenticaoLogin(String CPF, String PSW, emulador servidor) {
        servidor.selenium.open("/sistemas/login.php?cs=002");
        servidor.selenium.waitForPageToLoad("30000");
        servidor.selenium.type("cpf", CPF);
        servidor.selenium.type("senha", PSW);
        servidor.selenium.click("imageField");
    }

    public boolean setLogin(String CPF, String PSW, emulador servidor) {
        this.autenticaoLogin(CPF, PSW, servidor);
        String enderecoAtual = servidor.driver.getCurrentUrl();
        String erro1 = "https://www.intranet.ufsj.edu.br/sistemas/contac/verifica_dados.php";
        String erro2 = "https://www.intranet.ufsj.edu.br/sistemas/login.php?cs=002";
        if (enderecoAtual.equals(erro1) || enderecoAtual.equals(erro2)) {
            return false;
        } else {
            return true;
        }
    }

    public List<String> getInfoPrincipal(emulador servidor) {
        servidor.selenium.open("/sistemas/contac/index_contac.php");
        List<WebElement> dados = servidor.driver.findElements(By.className("fonte"));
        int index = 0;
        List<String> retorno = new ArrayList<String>();
        while (index < 4) {
            retorno.add(dados.get(index).getText());
            index++;
        }
        return retorno;
    }

    public ArrayList<Integer> getPeriodo(emulador servidor) {
        ArrayList<Integer> retorno = new ArrayList<Integer>();
        servidor.selenium.open("/sistemas/contac/index_contac.php");
        String curriculo = servidor.selenium.getText("//tr[3]/td[3]/span[2]");
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int atual = Integer.parseInt(curriculo);
        retorno.add(atual);
        retorno.add(month);
        int j = year - atual;
        retorno.add(j);
        return retorno;
    }

    public ArrayList<String> getDisciplinaFalta(emulador servidor, String ano, String semestre) {
        ArrayList<String> retorno = new ArrayList<String>();
        ArrayList<Integer> infoSaida = new ArrayList<Integer>();
        infoSaida = this.getPeriodo(servidor);
        int atual = infoSaida.get(0);
        int month = infoSaida.get(1);
        int j = infoSaida.get(2);
        while (j >= 0) {
            retorno.add(String.valueOf(atual));
            retorno.add("Primeiro");
            servidor.selenium.open("/sistemas/contac/escolher_ano_semestre1.php");
            WebElement element1 = servidor.driver.findElement(By.xpath("//option[@value=" + atual + "]"));
            element1.setSelected();
            WebElement element2 = servidor.driver.findElement(By.xpath("//option[@value='1']"));
            element2.setSelected();
            servidor.selenium.click("B1");
            servidor.selenium.waitForPageToLoad("30000");
            int i = 2;
            while (servidor.selenium.isElementPresent("//tr[" + i + "]/td[2]/div/span")) {
                String txt = servidor.selenium.getText("//tr[" + i + "]/td[2]/div/span");
                retorno.add(txt);
                i++;
            }
            if ((month <= 7) || (j != 0)) {
                retorno.add("Segundo");
                servidor.selenium.open("/sistemas/contac/escolher_ano_semestre1.php");
                WebElement element11 = servidor.driver.findElement(By.xpath("//option[@value=" + atual + "]"));
                element11.setSelected();
                WebElement element21 = servidor.driver.findElement(By.xpath("//option[@value='2']"));
                element21.setSelected();
                servidor.selenium.click("B1");
                servidor.selenium.waitForPageToLoad("30000");
                i = 2;
                while (servidor.selenium.isElementPresent("//tr[" + i + "]/td[2]/div/span")) {
                    String txt = servidor.selenium.getText("//tr[" + i + "]/td[2]/div/span");
                    retorno.add(txt);
                    i++;
                }
            }
            atual++;
            j--;
        }
        return retorno;
    }

    public ArrayList<String> getDisciplinaFaltaIndividual(emulador servidor, String ano) {
        ArrayList<String> retorno = new ArrayList<String>();
        ArrayList<Integer> infoSaida = new ArrayList<Integer>();
        infoSaida = this.getPeriodo(servidor);
        int atual = infoSaida.get(0);
        int month = infoSaida.get(1);
        servidor.selenium.open("/sistemas/contac/escolher_ano_semestre1.php");
        WebElement element1 = servidor.driver.findElement(By.xpath("//option[@value=" + ano + "]"));
        element1.setSelected();
        WebElement element2 = servidor.driver.findElement(By.xpath("//option[@value='1']"));
        element2.setSelected();
        servidor.selenium.click("B1");
        servidor.selenium.waitForPageToLoad("30000");
        retorno.add("Primeiro");
        int i = 2;
        while (servidor.selenium.isElementPresent("//tr[" + i + "]/td[2]/div/span")) {
            String txt = servidor.selenium.getText("//tr[" + i + "]/td[2]/div/span");
            retorno.add(txt);
            i++;
        }
        if (month <= 7) {
            retorno.add("Segundo");
            servidor.selenium.open("/sistemas/contac/escolher_ano_semestre1.php");
            WebElement element11 = servidor.driver.findElement(By.xpath("//option[@value=" + atual + "]"));
            element11.setSelected();
            WebElement element21 = servidor.driver.findElement(By.xpath("//option[@value='2']"));
            element21.setSelected();
            servidor.selenium.click("B1");
            servidor.selenium.waitForPageToLoad("30000");
            i = 2;
            while (servidor.selenium.isElementPresent("//tr[" + i + "]/td[2]/div/span")) {
                String txt = servidor.selenium.getText("//tr[" + i + "]/td[2]/div/span");
                retorno.add(txt);
                i++;
            }
        }
        return retorno;
    }
}
