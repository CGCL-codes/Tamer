package org.subrecord.api;

import static org.subrecord.util.Commons.getHttpDeleteResponse;
import static org.subrecord.util.Commons.getHttpGetResponse;
import static org.subrecord.util.Commons.getHttpPostResponse;
import static org.subrecord.util.Commons.glue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.subrecord.Constants;
import org.subrecord.api.exception.RecordNotFoundException;
import org.subrecord.api.exception.SubRecordApiException;
import org.subrecord.impl.RoundRobinLoadBalancer;
import org.subrecord.index.EqualsConstraint;
import org.subrecord.index.IndexHelper;
import org.subrecord.index.IndexLevel;
import org.subrecord.model.Record;
import org.subrecord.util.Commons;
import org.subrecord.util.JsonHelper;
import org.subrecord.util.LoadBalancer;

/**
 * @author przemek
 * 
 */
public class SubRecordClient {

    protected static final Logger LOG = Logger.getLogger(SubRecordClient.class);

    private LoadBalancer<InetSocketAddress> loadBalancer;

    public SubRecordClient(String... servers) {
        InetSocketAddress[] addresses = new InetSocketAddress[servers.length];
        for (int i = 0; i < servers.length; i++) {
            String[] t = servers[i].split(":");
            addresses[i] = new InetSocketAddress(t[0], Integer.valueOf(t[1]));
        }
        this.loadBalancer = new RoundRobinLoadBalancer<InetSocketAddress>(addresses);
    }

    public boolean put(Serializable domain, Serializable table, Serializable id, Record record) throws SubRecordApiException {
        InetSocketAddress address = loadBalancer.getNext();
        try {
            String uri = Commons.glue("http://", address.getHostName(), ":", address.getPort(), "/storage/", domain, "/", table, "/", id);
            LOG.debug(uri);
            PostMethod response = getHttpPostResponse(uri, JsonHelper.toJson(record));
            if (response.getStatusCode() != 200) {
                throw new SubRecordApiException(response);
            }
            return response.getResponseBodyAsString().contains("true");
        } catch (IOException e) {
            LOG.error(e);
            throw new SubRecordApiException(e);
        }
    }

    public Record get(Serializable domain, Serializable table, Serializable id) throws SubRecordApiException {
        InetSocketAddress address = loadBalancer.getNext();
        try {
            GetMethod response = getHttpGetResponse(glue("http://", address.getHostName(), ":", address.getPort(), "/storage/", domain, "/", table, "/", id));
            if (response.getStatusCode() != 200) {
                if (response.getStatusCode() == 404) {
                    throw new RecordNotFoundException(glue("Domain=", domain, ", Table=", table, ", Id=", id));
                }
                throw new SubRecordApiException(response);
            }
            return JsonHelper.fromJson(response.getResponseBodyAsString());
        } catch (IOException e) {
            LOG.error(e);
            throw new SubRecordApiException(e);
        }
    }

    public boolean remove(Serializable domain, Serializable table, Serializable id) throws SubRecordApiException {
        InetSocketAddress address = loadBalancer.getNext();
        try {
            DeleteMethod response = getHttpDeleteResponse(glue("http://", address.getHostName(), ":", address.getPort(), "/storage/", domain, "/", table, "/", id));
            if (response.getStatusCode() != 200) {
                throw new SubRecordApiException(response);
            }
            return response.getResponseBodyAsString().contains("true");
        } catch (IOException e) {
            LOG.error(e);
            throw new SubRecordApiException(e);
        }
    }

    public List<Record> find(IndexLevel level, Serializable domain, Serializable table, int limit, EqualsConstraint... constraints) throws SubRecordApiException {
        InetSocketAddress address = loadBalancer.getNext();
        try {
            GetMethod response = getHttpGetResponse(glue("http://", address.getHostName(), ":", address.getPort(), "/", IndexHelper.levelToPath(level), "/", domain, "/", table, "/?", Constants.LIMIT, "=", limit, toUri(constraints)));
            if (response.getStatusCode() != 200) {
                throw new SubRecordApiException(response);
            }
            return JsonHelper.fromJsonToList(response.getResponseBodyAsString());
        } catch (IOException e) {
            LOG.error(e);
            throw new SubRecordApiException(e);
        }
    }

    private Object toUri(EqualsConstraint[] constraints) {
        final StringBuilder uri = new StringBuilder();
        for (EqualsConstraint constraint : constraints) {
            uri.append("&").append(constraint.getField()).append("=").append(constraint.getValue());
        }
        return uri.toString();
    }

    public long count(Serializable domain, Serializable table, Serializable id) throws SubRecordApiException {
        throw new SubRecordApiException("Not Implemented Yet");
    }

    public void get(RecordCallback callback, Serializable domain, Serializable table) throws SubRecordApiException {
        get(callback, domain, table, 0, Long.MAX_VALUE);
    }

    public void get(RecordCallback callback, Serializable domain, Serializable table, long offset, long recordsNum) throws SubRecordApiException {
        InetSocketAddress address = loadBalancer.getNext();
        try {
            GetMethod response = getHttpGetResponse(glue("http://", address.getHostName(), ":", address.getPort(), "/storage/", domain, "/", table, "/", offset, "/", recordsNum));
            BufferedReader br = new BufferedReader(new InputStreamReader(response.getResponseBodyAsStream()));
            String line = null;
            while ((line = br.readLine()) != null) {
                callback.recordLoaded((Record) JsonHelper.fromJson(line));
            }
            br.close();
        } catch (IOException e) {
            LOG.error(e);
            throw new SubRecordApiException(e);
        }
    }

    public Iterator<Record> get(Serializable domain, Serializable table, String[] view) throws SubRecordApiException {
        throw new SubRecordApiException("Not Implemented Yet");
    }

    public Iterator<Record> get(Serializable domain, Serializable table, Serializable id, String[] view) throws SubRecordApiException {
        throw new SubRecordApiException("Not Implemented Yet");
    }
}
