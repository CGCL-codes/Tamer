package br.nic.connector.links;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import br.nic.connector.general.Constants;
import br.nic.connector.general.SimpleLog;
import br.nic.connector.general.Utils;
import br.nic.connector.generics.GenericTest;

/**
 * Implementa os testes de contagem de links.
 * @author Heitor, Pedro
 */
public class LinksCounter extends GenericTest {

    /**
	 * Testa um element do tipo File, que deve conter uma lista de Ids de Hosts com os 
	 * respectivos links acessados por estes. Estes links são contados, e adicionados, já nesta classe,
	 * no banco de dados, incrementando, de forma Thread-safe, as quantidades previamente armazenadas.
	 */
    @Override
    public Object testThisElement(Object element) {
        if (!(element instanceof File)) {
            SimpleLog.getInstance().writeLog(3, "Dados de entrada incorretos em LinksTester.");
            return null;
        }
        File f = (File) element;
        BufferedReader linkList;
        try {
            linkList = new BufferedReader(new FileReader(f));
        } catch (FileNotFoundException e) {
            SimpleLog.getInstance().writeLog(3, "Erro ao abrir o arquivo: " + f.getName());
            return null;
        }
        Map<String, Integer> mapExt = new HashMap<String, Integer>();
        Map<String, Integer> mapDom = new HashMap<String, Integer>();
        try {
            while (linkList.ready()) {
                String[] line = linkList.readLine().split(" ");
                if (line.length > 3) {
                    Long pageWireId = Long.parseLong(line[0]);
                    Long hostId = AutomatedLinksCounter.getInstance().getHostFromPage(pageWireId);
                    if (pageWireId == null) {
                        System.out.println("ID NULL!!!????");
                        continue;
                    }
                    String url = line[3];
                    String extensao = null;
                    if (url.contains(".")) {
                        extensao = Utils.getExtMinusParams(url);
                    }
                    if (extensao == null) {
                        extensao = Constants.LINKS_EXTENSION_NONE;
                    }
                    String keyExt = hostId + " " + extensao;
                    Integer qtdeExt = mapExt.get(keyExt);
                    if (qtdeExt == null) {
                        mapExt.put(keyExt, 1);
                    } else {
                        mapExt.put(keyExt, qtdeExt + 1);
                    }
                    boolean local = Utils.isLinkLocal(url);
                    String dominio = "";
                    if (local) {
                        dominio = "*";
                    } else {
                        dominio = Utils.getDomain(Utils.getSubdomain(Utils.getPageHost(url)));
                    }
                    String keyDom = hostId + " " + dominio + " " + local;
                    Integer qtdeDom = mapDom.get(keyDom);
                    if (qtdeDom == null) {
                        mapDom.put(keyDom, 1);
                    } else {
                        mapDom.put(keyDom, qtdeDom + 1);
                    }
                    if (Constants.XML_EXTLIST.contains(extensao)) {
                        AutomatedLinksCounter.getInstance().writeLinkDownloadList(pageWireId, hostId, local, url);
                    }
                }
            }
            System.out.println("Tamanho Map Host: " + mapDom.size());
            System.out.println("Tamanho Map Ext:  " + mapExt.size());
            System.out.println("Finalizando:      " + f.getName());
            AutomatedLinksCounter.getInstance().consolidateMaps(mapExt, mapDom);
        } catch (IOException e) {
            SimpleLog.getInstance().writeLog(3, "Erro, dados corrompidos no arquivo: " + f.getName());
        } finally {
            try {
                linkList.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mapExt = null;
            mapDom = null;
        }
        return null;
    }
}
