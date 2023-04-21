package org.jcvi.vics.web.gwt.detail.client.service.cluster;

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.jcvi.vics.model.common.SortArgument;
import org.jcvi.vics.web.gwt.detail.client.DetailServiceAsync;

/**
 * Created by IntelliJ IDEA.
 * User: cgoina
 * Date: Nov 15, 2007
 * Time: 10:14:14 PM
 */
public interface ClusterDetailServiceAsync extends DetailServiceAsync {

    void getProteinCluster(String clusterAcc, AsyncCallback callback);

    void getClusterAnnotations(String clusterAcc, SortArgument[] sortArgs, AsyncCallback callback);

    void getPagedCoreClustersFromFinalCluster(String finalClusterAcc, int startIndex, int numRows, SortArgument[] sortArgs, AsyncCallback callback);

    void getPagedNRSeqMembersFromCluster(String clusterAcc, int startIndex, int numRows, SortArgument[] sortArgs, AsyncCallback callback);

    void getPagedSeqMembersFromCluster(String clusterAcc, int startIndex, int numRows, SortArgument[] sortArgs, AsyncCallback callback);

    void getNumMatchingRepsFromClusterWithAnnotation(String clusterAcc, String annotationId, AsyncCallback callback);

    void getPagedMatchingRepsFromClusterWithAnnotation(String clusterAcc, String annotationId, int startIndex, int numRows, SortArgument[] sortArgs, AsyncCallback callback);
}
