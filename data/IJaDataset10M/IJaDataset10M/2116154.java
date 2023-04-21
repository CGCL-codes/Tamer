package org.squabble.web.comment;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.squabble.domain.Account;
import org.squabble.domain.Comment;
import org.squabble.domain.Flag;
import org.squabble.service.CommentService;
import org.squabble.service.FlagService;
import org.squabble.service.TagService;

@Controller
public class CommentController {

    @Autowired
    @Qualifier("validator")
    Validator validator;

    @Autowired
    CommentService commentService;

    @Autowired
    TagService tagService;

    @Autowired
    FlagService flagService;

    @RequestMapping(value = "/comments/load", method = RequestMethod.POST)
    public String load(@RequestParam("id") Long remarkId, @RequestParam(value = "loadChildren", required = false) Boolean loadChildren, ModelMap model) {
        Comment comment = commentService.getComment(remarkId);
        model.addAttribute("comment", comment);
        model.addAttribute("loadChildren", loadChildren);
        if (comment.getRoot() == null) {
            return "ajax:comment.load";
        }
        return "ajax:comment.load.single";
    }

    @RequestMapping(value = "/comments", method = RequestMethod.GET)
    public String execute(ModelMap model) {
        CommentSubmitCommand csc = new CommentSubmitCommand();
        model.addAttribute("commentSubmitCommand", csc);
        List<Comment> comments = commentService.getComments();
        model.addAttribute("comments", comments);
        List<Flag> flags = flagService.getUserSelectableFlags();
        model.addAttribute("flags", flags);
        return "comments";
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/comments", method = RequestMethod.POST)
    public String processSubmit(@ModelAttribute("commentCommand") CommentSubmitCommand rsc) {
        Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        commentService.postComment(rsc.getArticleId(), rsc.getParentId(), rsc.getTitle(), rsc.getText(), rsc.getTags(), rsc.getFlags(), account);
        return "redirect:/comments";
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/comments/tag", method = RequestMethod.POST)
    public String processTags(@ModelAttribute("tagCommand") TagCommand tc, ModelMap model) {
        Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        commentService.updateCommentTags(tc.getCommentId(), tc.getTags(), account);
        Comment comment = commentService.getComment(tc.getCommentId());
        model.addAttribute("comment", comment);
        return "ajax:comment.load.tags";
    }
}
