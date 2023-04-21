package com.google.code.jqwicket.ui.blockui;

import com.google.code.jqwicket.IJQWidget;
import com.google.code.jqwicket.api.IJQFunction;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * Implementation of <a href="http://jquery.malsup.com/block/>BlockUI</a> jquery
 * plugin.
 * 
 * @author mkalina
 * 
 */
public interface IBlockUI extends IJQWidget<BlockUIOptions> {

    static final CharSequence JQ_COMPONENT_NAME = "block";

    /**
	 * Blocks complete screen using default options.
	 */
    IJQFunction block();

    /**
	 * Blocks complete screen using default options.
	 * 
	 * @param ajaxRequestTarget
	 */
    void block(AjaxRequestTarget ajaxRequestTarget);

    /**
	 * Blocks complete screen using custom options.
	 */
    IJQFunction block(BlockUIOptions options);

    /**
	 * Blocks complete screen using custom options.
	 * 
	 * @param ajaxRequestTarget
	 */
    void block(BlockUIOptions options, AjaxRequestTarget ajaxRequestTarget);

    /**
	 * Blocks specific ui element using default options.
	 */
    IJQFunction block(CharSequence selector);

    /**
	 * Blocks specific ui element using default options.
	 * 
	 * @param ajaxRequestTarget
	 */
    void block(CharSequence selector, AjaxRequestTarget ajaxRequestTarget);

    /**
	 * Blocks specific ui element using custom options.
	 */
    IJQFunction block(CharSequence selector, BlockUIOptions options);

    /**
	 * Blocks specific ui element using custom options.
	 * 
	 * @param ajaxRequestTarget
	 */
    void block(CharSequence selector, BlockUIOptions options, AjaxRequestTarget ajaxRequestTarget);

    /**
	 * Blocks specific ui element using default options.
	 */
    IJQFunction block(Component component);

    /**
	 * Blocks specific ui element using default options.
	 * 
	 * @param ajaxRequestTarget
	 */
    void block(Component component, AjaxRequestTarget ajaxRequestTarget);

    /**
	 * Blocks specific ui element using custom options.
	 */
    IJQFunction block(Component component, BlockUIOptions options);

    /**
	 * Blocks specific ui element using custom options.
	 * 
	 * @param ajaxRequestTarget
	 */
    void block(Component component, BlockUIOptions options, AjaxRequestTarget ajaxRequestTarget);

    /**
	 * Unblocks complete screen using default options.
	 */
    IJQFunction unblock();

    /**
	 * Unblocks complete screen using default options.
	 * 
	 * @param ajaxRequestTarget
	 */
    void unblock(AjaxRequestTarget ajaxRequestTarget);

    /**
	 * Unblocks complete screen using custom options.
	 */
    IJQFunction unblock(BlockUIOptions options);

    /**
	 * Unblocks complete screen using custom options.
	 * 
	 * @param ajaxRequestTarget
	 */
    void unblock(BlockUIOptions options, AjaxRequestTarget ajaxRequestTarget);

    /**
	 * Unblocks specific ui element using default options.
	 */
    IJQFunction unblock(CharSequence selector);

    /**
	 * Unblocks specific ui element using default options.
	 * 
	 * @param ajaxRequestTarget
	 */
    void unblock(CharSequence selector, AjaxRequestTarget ajaxRequestTarget);

    /**
	 * Unblocks specific ui element using custom options.
	 */
    IJQFunction unblock(CharSequence selector, BlockUIOptions options);

    /**
	 * Unblocks specific ui element using custom options.
	 * 
	 * @param ajaxRequestTarget
	 */
    void unblock(CharSequence selector, BlockUIOptions options, AjaxRequestTarget ajaxRequestTarget);

    /**
	 * Unblocks specific ui element using default options.
	 */
    IJQFunction unblock(Component component);

    /**
	 * Unblocks specific ui element using default options.
	 * 
	 * @param ajaxRequestTarget
	 */
    void unblock(Component component, AjaxRequestTarget ajaxRequestTarget);

    /**
	 * Unblocks specific ui element using custom options.
	 */
    IJQFunction unblock(Component component, BlockUIOptions options);

    /**
	 * Unblocks specific ui element using custom options.
	 * 
	 * @param ajaxRequestTarget
	 */
    void unblock(Component component, BlockUIOptions options, AjaxRequestTarget ajaxRequestTarget);
}
