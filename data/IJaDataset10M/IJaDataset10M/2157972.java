package wp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public final class BlogIO {

    public static Blog read(String fileName) throws UnsupportedEncodingException, FileNotFoundException, DocumentException, ParseException {
        Blog blog = new Blog();
        SAXReader reader = new SAXReader();
        Document doc = reader.read(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
        for (Node node : (List<Node>) doc.selectNodes("//channel/wp:category")) {
            blog.addCategory(node.valueOf("wp:cat_name"));
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Node n : (List<Node>) doc.selectNodes("//channel/item")) {
            Post post = new Post();
            post.setCategory(n.valueOf("category"));
            post.setTitle(n.valueOf("title"));
            post.setContent(n.valueOf("content:encoded"));
            post.setStatus(n.valueOf("wp:status").equals("publish") ? PostStatus.PUBLISH : PostStatus.DRAFT);
            post.setType(n.valueOf("wp:post_type").equals("post") ? PostType.POST : PostType.PAGE);
            post.setCreatedAt(sdf.parse(n.valueOf("wp:post_date")));
            for (Node c : (List<Node>) n.selectNodes("wp:comment")) {
                Comment comment = new Comment();
                comment.setAuthor(c.valueOf("wp:comment_author"));
                comment.setEmail(c.valueOf("wp:comment_author_email"));
                comment.setUrl(c.valueOf("wp:comment_author_url"));
                comment.setHost(c.valueOf("wp:comment_author_IP"));
                comment.setContent(c.valueOf("wp:comment_content"));
                comment.setCreatedAt(sdf.parse(c.valueOf("wp:comment_date")));
                post.addComment(comment);
            }
            blog.addPost(post);
        }
        return blog;
    }
}
