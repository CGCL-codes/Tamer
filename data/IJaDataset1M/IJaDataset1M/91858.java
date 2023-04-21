package com.softserve.mproject.client.common.network;

import com.softserve.mproject.client.common.Client;
import com.softserve.mproject.client.common.Notify;
import com.softserve.mproject.utils.exceptions.ClientException;
import com.softserve.mproject.client.common.utils.Config;
import com.softserve.mproject.domain.entity.CatalogData;
import com.softserve.mproject.domain.entity.City;
import com.softserve.mproject.domain.entity.Country;
import com.softserve.mproject.domain.entity.Street;
import com.softserve.mproject.utils.xml.MessageGenerator;
import com.softserve.mproject.utils.constants.Command;
import com.softserve.mproject.utils.xml.MessageParser;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

public class ClientImpl implements Client {

    protected Notify notify;

    private HttpClient client;

    public ClientImpl(Notify notify) throws ClientException {
        this.notify = notify;
        client = new HttpClient();
    }

    @Override
    public void add(Collection data) throws ClientException {
        send(generate(data, Command.ADD));
        receive();
    }

    @Override
    public void delete(Collection data) throws ClientException {
        send(generate(data, Command.DELETE));
        receive();
    }

    @Override
    public void update(Collection data) throws ClientException {
        send(generate(data, Command.UPDATE));
        receive();
    }

    @Override
    public synchronized void loadAll() throws ClientException {
        receive();
    }

    public String generate(Collection data, Command cmd) {
        Map map = new HashMap();
        for (Object o : data) {
            map.put(o, cmd.getCommand());
        }
        return MessageGenerator.generate(map);
    }

    protected void receive() throws ClientException {
        try {
            HttpMethod method = new GetMethod("http://" + Config.httpAddress + ":" + Config.httpPort + "/" + Config.httpUrl);
            client.executeMethod(method);
            Map map = MessageParser.parse(new InputStreamReader(method.getResponseBodyAsStream()));
            if (notify != null) {
                notify.update(map.keySet());
            }
        } catch (HttpException ex) {
            throw new ClientException("Problem in connection to host [" + Config.httpAddress + ":" + Integer.toString(Config.httpPort) + "]");
        } catch (IOException ex) {
            throw new ClientException("Problem in data transfering");
        }
    }

    protected void send(String xmlMessage) throws ClientException {
        try {
            PostMethod postMethod = new PostMethod("http://" + Config.httpAddress + ":" + Config.httpPort + "/" + Config.httpUrl);
            postMethod.setRequestBody(xmlMessage);
            postMethod.setRequestHeader("Content-type", "text/xml; charset=UTF-8");
            client.executeMethod(postMethod);
            postMethod.releaseConnection();
        } catch (HttpException ex) {
            throw new ClientException("Problem in connection to host [" + Config.httpAddress + ":" + Integer.toString(Config.httpPort) + "]");
        } catch (IOException ex) {
            throw new ClientException("Problem in data transfering");
        }
    }

    /** 
     * !!! Mock function !!!
     * Refreshes dictionaries to actual state
     * @param streetDictionary - created collection to handle streets
     * @param cityDictionary - created collection to handle cities
     * @param countryDictionary - created collection to handle countries
     */
    @Override
    public void getDictionaries(Collection streetDictionary, Collection cityDictionary, Collection countryDictionary) {
        streetDictionary.clear();
        cityDictionary.clear();
        countryDictionary.clear();
        String[][] data = { { "Lenina", "Stalina", "Elm", "Backer", "Kupfergraben" }, { "Kiev", "Moscow", "NY", "London", "Berlin" }, { "Ukraine", "Russia", "USA", "UK", "Germany" } };
        for (int i = 1; i < 6; i++) {
            CatalogData[] domains = { new Street(), new City(), new Country() };
            for (int j = 0; j < 3; j++) {
                domains[j].setId(i);
                domains[j].setName(data[j][i - 1]);
            }
            streetDictionary.add(domains[0]);
            cityDictionary.add(domains[1]);
            countryDictionary.add(domains[2]);
        }
    }
}
