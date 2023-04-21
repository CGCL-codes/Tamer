package org.fpse.refresher.iitfoundation;

import java.net.HttpURLConnection;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.fpse.forum.ForumConfiguration;
import org.fpse.schedular.refresh.impl.question.sequential.SequentialQuestionIdDownloadParameters;

/**
 * Created on Aug 30, 2008 1:15:00 PM by Ajay
 */
public class IITFoundationDownloadParameters extends SequentialQuestionIdDownloadParameters {

    public IITFoundationDownloadParameters(final long id, final ForumConfiguration configuration) {
        super(id, configuration);
    }

    @Override
    public boolean isSuccessCode(final org.apache.http.client.HttpClient client, final HttpUriRequest method, final int code) {
        return code == HttpURLConnection.HTTP_OK;
    }

    @Override
    public void postProcess(final HttpClient client, final HttpUriRequest request, final HttpResponse response) {
    }

    @Override
    public boolean shouldReturnStream() {
        return true;
    }
}
