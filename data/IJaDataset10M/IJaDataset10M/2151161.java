package com.tomczarniecki.s3.rest;

import com.tomczarniecki.s3.Lists;
import com.tomczarniecki.s3.S3Bucket;
import java.io.InputStream;
import java.util.List;

public class S3BucketParser {

    public List<S3Bucket> parse(InputStream input) {
        XPathNode doc = new XPathNode(input);
        List<S3Bucket> results = Lists.create();
        for (XPathNode node : doc.queryForNodes("//aws:Bucket")) {
            results.add(new S3Bucket(node.queryForText("./aws:Name")));
        }
        return results;
    }
}
